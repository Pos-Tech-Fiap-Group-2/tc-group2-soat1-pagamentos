package com.techchallenge.pagamentos.adapter.mapper.api;

import com.techchallenge.pagamentos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pagamentos.adapter.driver.model.input.PagamentoInput;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pagamentos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pagamentos.core.domain.entities.Pagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MercadoPagoApiMapperTest {
    @InjectMocks
    private MercadoPagoApiMapper mapper;

    @Mock
    private ModelMapper modelMapper;


    @Test
    void toDomainObjectTest() {
        PagamentoPixResponseDTO pagamentoPixResponseDTO = new PagamentoPixResponseDTO(123L, "Approved", "Detalhes",
                "Pix", "qrCodeBase64", "qrCode");

        when(modelMapper.map(pagamentoPixResponseDTO, PagamentoPixResponseDTO.class)).thenReturn(pagamentoPixResponseDTO);
        Assertions.assertNotNull(mapper.toDomainObject(pagamentoPixResponseDTO));
    }

    @Test
    void toDomainObjectPagamentoResponseDTOTest() {
        PagamentoResponseDTO pagamentoResponseDTO = new PagamentoResponseDTO(123L, "Approved", "Detalhes",
                "Pix");

        when(modelMapper.map(pagamentoResponseDTO, PagamentoResponseDTO.class)).thenReturn(pagamentoResponseDTO);
        Assertions.assertNotNull(mapper.toDomainObject(pagamentoResponseDTO));
    }


}
