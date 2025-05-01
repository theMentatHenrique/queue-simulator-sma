package br.com.pucrs

import br.com.pucrs.factory.QueueFactory
import br.com.pucrs.factory.RandomNumbersManager

fun main() {
    var factory = QueueFactory()
    val queues = factory.readQueuesAndNetworksFromYaml("/Users/henriquefeijopaim/Documents/Pessoal/queue-simulator-sma/src/main/kotlin/factory/model.yml")
    var numbers = RandomNumbersManager.generateNumbersByLimit(limit = factory.getrandom(), factory.getArrivalTime().toInt())

    val scaler = Scheduler(queues = queues, randomNums = numbers.toMutableList())
    scaler.start(factory.getArrivalTime())
}

interface Event {
    fun getCurrentEventTime() : Float
    fun isArrival() : Boolean
    fun isPassage() : Boolean
    fun isExitQueue() : Boolean
    fun isExitSystem() : Boolean
    fun queueArrival() : String
    fun queueExit() : String
}