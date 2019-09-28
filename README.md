# trabalho-ambientes

projeto spring-boot consumidor para sumarização de valores do bolsa familia por estado

Serviço se escreve em uma fila esperando receber o estado por parametro no body e processa o arquivo setado

Passo a passo para execução:

1. clonar projeto

2. executar como spring-boot application

3. esperar uma requicisao na fila `fiap.scj.mensagens` do RabbitMQ
