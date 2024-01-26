# language: pt
Funcionalidade: API - Pagamentos

  Cenário: Efetuar pagamento
    Quando realizar pagamento
    Então o pagamento é efetuado com sucesso

  Cenário: Consultar tipos de pagamentos aceitos na plataforma
    Quando solicitar registros de tipos de pagamentos na plataforma
    Então os tipos de pagamentos são exibidos com sucesso

  Cenário: Confirmação de status do pagamento
    Dado um evento de pagamento aprovado ou recusado
    Quando receber evento de pagamento aprovado ou recusado
    Então deve confirmar pagamento

  Cenário: Consultar status do pagamento
    Quando requisitar a consulta do status do pagamento
    Então o status do pagamento deve ser exibido com sucesso

