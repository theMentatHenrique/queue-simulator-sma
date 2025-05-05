# Simulador de roteamento de filas

## Autores

* [Henrique Feijó Paim](https://github.com/seu-usuario-github](https://github.com/theMentatHenrique))
* [Leonardo Ruppert](https://github.com/usuario-colaborador-1](https://github.com/yazuc))

## Implementação

O trabalho foi desenvolvido em Kotlin, usando com base os princípios de orientação a objetos e SOLID
utilizando os pseudo-códigos disponibilizados pelo professor Afonso Correa - PUCRS

### Estrutura do Projeto

Foram criadas as seguintes classes para o funcionamento:
* Event : Mais básica, contém os dados pertinentes ao evento (filas de origem, destino e tempo do evento)
* Queue: Tem seus valores controlados pelo Scheduler, esta classe persiste os estados de uma fila usando como base os atributos definidos pelo professor
* Scheduler : Nosso escalonador, a classe core da aplicação, onde esta toda lógica de manipulação e criação de eventos, bem como delegar para as filas respectivas e
  encerrar a simulação. Possui os algoritmos de chegada, saída e passagem vistos em aula

### Execução
* Por algum motivo esta dando erro ao rodar o jar criado, sendo necessário executar pela IDE(IntelliJ ou Android Studio)
* Para iniciar a simulação basta alterar o arquivo model.yml presente(Não pode trocar o nome)
* A estrutura do yml segue a mesma do disponibilizado pela plataforma

### Limitações
* O arquivo yml está um pouco rígido, os valores das filas devems ser tratados como ponto flutuante e não temos um sistema de especificar o arquivo por linha de comando
* Também não tratamos mais de 1 evento chegando de fora do sistema, por isso lemos apenas um atributo do campo "Arrivals"
* Os valores mantem certa consistência, porém divergem quanto ao número de clientes perdidos na fila (em proporções maiores que o simulador dado como exemplo) mas mantem certa coesão
* Pelos conflitos com uma versão anterior, o código encontra-se na branch dev


