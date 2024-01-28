package com.techchallenge.pagamentos.adapter.driver.exceptionhandler;


import java.time.OffsetDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.techchallenge.pagamentos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pagamentos.core.domain.exception.NegocioException;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String ERRO_INESPERADO = "Ocorreu um erro interno inesperado no sistema.";
	private static final String CORPO_REQUISICAO_INVALIDO = "Corpo da requisição está inválido. Verifique erro de sintaxe";

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.MENSAGEM_INCONSISTENTE;
		String detail = CORPO_REQUISICAO_INVALIDO;

		Problem problem = this.createProblemBuilder(status, problemType, detail).build();

		return this.handleExceptionInternal(ex, problem, new HttpHeaders(),
				status, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;

		String detail = ERRO_INESPERADO;
		Problem problem = createProblemBuilder(status, problemType, detail).build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest webRequest) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;

		Problem problem = this.createProblemBuilder(status, problemType, e.getMessage()).build();

		return this.handleExceptionInternal(e, problem, new HttpHeaders(),
				status, webRequest);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e, WebRequest webRequest) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

		Problem problem = this.createProblemBuilder(status, problemType, e.getMessage()).build();

		return this.handleExceptionInternal(e, problem, new HttpHeaders(),
				status, webRequest);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
															 HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Problem.builder()
					.title(status.getReasonPhrase())
					.status(status.value()).userMessage(ERRO_INESPERADO)
					.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail).timestamp(OffsetDateTime.now());
	}
}