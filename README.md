# Documentação - Tech Challenge - Grupo 2 SOAT1 - PosTech - Arquitetura de Software - FIAP
Repositório para o microsserviço de pagamentos do desafio do Tech Challenge da Pós-gradução em Software Architecture pela FIAP.

## Introdução
Uma lanchonete de bairro que está expandido sua operação devido seu grande sucesso. Porém, com a expansão e sem um sistema de controle de pedidos, o atendimento aos clientes pode ser caótico e confuso.
Para solucionar o problema, a lanchonete irá investir em um sistema de autoatendimento de fast food, que é composto por uma série de dispositivos e interfaces que permitem aos clientes selecionar e fazer pedidos sem precisar interagir com um atendente.

## Membros do Grupo 2
- [Danilo Monteiro](https://github.com/dmonteirosouza)
- [Marcel Cozono](https://github.com/macozono)
- [Viviane Scarlatti](https://github.com/viviane-scarlatti)
- [Vinicius Furin](https://github.com/VFurin)
- [Vitor Walcker](https://github.com/VitorWalcker)

# Compilação e geração de artefato
Antes de iniciar, certifique-se de que sua máquina atenda aos seguintes requisitos:<br/><br/>

**JDK 11 instalado:**<br/>
Certifique-se de ter o JDK 11 instalado em sua máquina.<br/><br/>

**Maven >= 3 instalado:**<br/>
Verifique se você tem o Maven instalado em sua máquina. Para verificar a versão do Maven instalada, execute o seguinte comando no terminal: **mvn -version**. Se o Maven não estiver instalado ou estiver em uma versão inferior à 3, faça o download e siga as instruções de instalação do site oficial do Maven ou de outra fonte confiável.<br/><br/>

**Clonando o projeto:**<br/>
Clone o repositório do projeto em seu ambiente local.<br/><br/>

**Compilando o projeto:**<br/>
Navegue até o diretório raiz do projeto no terminal e execute o seguinte comando para compilar o projeto usando o Maven:<br/><br/>

```sh
mvn clean install
```

Isso irá baixar as dependências do projeto, compilar o código-fonte e criar o artefato no diretório target com o nome **tc-group2-soat1-pagamentos.jar**.<br/>
Esse artefato será copiado para a imagem do container em momento de build durante a execução do docker-compose.

**Executando o projeto:**<br/>
Após a conclusão da etapa anterior, você pode executar o projeto seguindo as instruções específicas do projeto.

# Build da imagem do projeto
Caso seja necessária a geração de uma nova imagem, executar o comando no diretório raíz do projeto:
```sh
docker build --build-arg "JAR_FILE=tech-challenge-group2-soat1-pagamentos.jar" -t <usuario>/<imagem_nome>:<tag> .
```

## Documentação Swagger da API
A documentação em padrão Swagger está disponível em http://localhost:8080/api/swagger-ui.html.

## Execução do projeto via Postman
Basta clicar no link [diretório postman](src/main/resources/postman) onde está disponível o arquivo JSON contendo todos os endpoints configurados basta importá-lo via Postman e executar o passo-a-passo abaixo.

<a name="ancora"></a>
1. [Listar tipos de pagamento](#ancora1)
2. [Consultar pagamento MP](#ancora2)
3. [Gerar pagamento](#ancora3)
4. [Confirmar pagamento](#ancora4)

## Informações adicionais
Algumas informações adicionais sobre a construção da API

### Conexão com banco de dados
Na API foi adicionado um parâmetro condicional para verificação de uma variável de ambiente chamada **DB_HOST**. Essa variável é injetada no container em momento de construção e é indicada com a URL para acesso ao banco de dados. Caso não seja encontrado um valor para essa variável, o valor **localhost:3306** será utilizado como **default**.<br/>
Essa parametrização foi utilizada no caso se for necessário realizar testes via API localmente, sem a necessidade de estar inicializada no container.

### Habilitadores para geração de QR Code do Mercado Pago e configuração de Webhook
Na API foi disponibilizada a integração com o Mercado Pago para geração do QR Code e parametrização do Webhook para notificações relacionadas a posteriores eventos emitidos relacionados a solicitação de pagamento gerada.

#### Premissas para integração
Para integração com o Mercado Pago é necessário que se tenha uma conta gerada e uma aplicação no padrão MarketPlace e integração CheckoutTransparente.
Com a geração da aplicação no Mercado Pago é possível gerar as credenciais de teste que devem ser utilizadas na integração com a API para geração do método de pagamento e habilitação do Webhook.

#### Configuração
Para o pleno funcionamento da integração com o Mercado Pago foram configuradas duas variáveis de ambientes injetadas no properties da API, sendo elas:

- **MP_ACCESS_TOKEN:** Access token para autenticação na REST API do Mercado Pago para a geração do método de pagamento QR Code e retorno da estrutura de dados contendo a estrutura em base64 para a imagem do QR Code e chave para pagamento;
- **MP_NOTIFICATION_URL:** Endereço do Webhook que será acionada pela API do Mercado Pago quando qualquer evento relacionado ao método de pagamento gerado for atualizado.

Ambas as variáveis são disponibilizadas no ambiente através do provisionamento dos recursos no k8s.

**Importante!**
Devido a API estar sendo executada em ambiente local, será necessário dispor de um ambiente público no qual permita a configuração de webhooks para testes de integração. No caso dos testes realizados, foi utilizado o site https://webhook.site/.

### Profiles do Springboot
Foram criados dois profiles springboot para execução da API, sendo eles:

- **default**: A execução desse profile irá verificar pela conectividade com o banco de dados MySQL identificado pela variável de ambiente ou pelo localhost na porta 3306;
- **mock**: A execução desse profile irá iniciar o banco de dados H2 para testes. Esse profile pode ser utilizado em caso de testes unitários para persistência dos dados e memória.

# Endpoints disponíveis

<a id="ancora1"></a>
##### Listar tipos de pagamento
```sh
GET http://localhost:8080/api/pagamentos/tipos-pagamento
```

<a id="ancora2"></a>
##### Consultar status do pagamento de um pedido
```sh
GET http://localhost:8080/api/pagamentos/status/pagamentos/1/pedidos/1
```

<a id="ancora3"></a>
##### Gerar pagamento de um pedido
```sh
POST http://localhost:8080/api/pagamentos

Request body
{
  "tipoPagamentoId":  1,
  "pedidoId":  1,
  "valor":  "25.99",
  "cliente":  {
    "nome":  "Nome Teste",
    "email":  "email-teste@teste.com.br",
    "cpf":  68127322067
  }
}
```
Nesse momento será gerado o QR Code para pagamento do pedido via PIX.
O QR Code será gerado através da integração com a API do Mercado Pago e será retornado no payload da resposta da requisição. A partir desse ponto, as alterações ocorridas no status do pagamento originadas pelo Mercado Pago serão notificadas para o webhook configurado no projeto.

<a id="ancora4"></a>
##### Confirmar pagamento

Esse webhook para recebimento de eventos relacionados a mudança de estado do pagamento pelo Mercado Pago. Através do id do pagamento é realizada uma consulta na API do Mercado Pago para detalhe do status do método de pagamento e posterior atualização do status do pedido.

```sh
POST http://localhost:8080/api/pagamentos/mercadopago/notifications

Request body
{
	"data" : {
		"id": 1234567
	}
}
```

**Importante!**
- Após realizar requisição do pagamento, o status do pedido será alterado para **GERAÇÃO**. Esse status **não permite** mais manutenção no pedido.
- O pedido, após efetivar o pagamento, será encaminhado a cozinha para que seja preparado.
- Para alteração do status do pedido, utilizar os endpoints disponibilizados no recurso pedidos.

**Observação**
O tech challenge define que o método de pagamento pelo Mercado Pago seja através de PIX.
O webhook foi construído com a definição de suportar o recebimento de notificações perante a atualização de status sobre o pagamento gerado no Mercado Pago. Porém, não foi encontrada nenhuma documentação, **a caráter de teste**, que apresentasse uma chamada via API do Mercado Pago na qual simulasse a efetivação do pagamento. Sendo que o pagamento via PIX é necessário ser realizado por mediação de um instituição financeira.
Portanto, nesse ponto, foi construída a implementação do Webhook com o objetivo de **analisar o estado do pagamento recebido**. Com isso, realizar a atualização do pagamento do pedido dentro do sistema. Por não ter como atualizar de forma mockada esse retorno, esse endpoint sempre manterá o estado do pagamento atual para o pedido.

**OWASP Report**

[Relatório antes da correção da vulnerabilidade](https://github.com/Pos-Tech-Fiap-Group-2/tc-group2-soat1-pagamentos/blob/0160f45c5a07247d9f3b0f2a5e6fb0478a5cfa08/2024-03-13-ZAP-Report-geracao-confirmacao-pagamento-antes.pdf)


[Relatório depois da correção da vulnerabilidade](https://github.com/Pos-Tech-Fiap-Group-2/tc-group2-soat1-pagamentos/blob/0160f45c5a07247d9f3b0f2a5e6fb0478a5cfa08/2024-03-15-ZAP-Report-geracao-confirmacao-depois.pdf)

Obs: a vulnerabilidade corrigida tinha risco classificado pelo relatório como baixo.
