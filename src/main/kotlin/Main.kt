package br.com.pucrs

import br.com.pucrs.factory.QueueFactory
import br.com.pucrs.factory.RandomNumbersManager

fun main() {
    var factory = QueueFactory()
    val p = 2999413E9
    val queues = factory.readQueuesAndNetworksFromYaml("/Users/henriquefeijopaim/Documents/Pessoal/queue-simulator-sma/src/main/kotlin/factory/model-aux.yml")
    var numbers = RandomNumbersManager.generateNumbersByLimit(limit = factory.getrandom(), factory.getArrivalTime().toInt())

    val scaler = Scheduler(queues = queues, randomNums = numbers.toMutableList())
    scaler.start(factory.getArrivalTime())
}
