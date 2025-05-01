package br.com.pucrs

import br.com.pucrs.factory.QueueFactory
import br.com.pucrs.factory.RandomNumbersManager

fun main() {
    var numbers = RandomNumbersManager.generateAndSave("random_numbers.txt").toMutableList()
    var factory = QueueFactory()
    val queues = factory.readQueuesAndNetworksFromYaml("/Users/henriquefeijopaim/Documents/Pessoal/queue-simulator-sma/src/main/kotlin/factory/model.yml")
    factory.getArrivalTime()

   /* val scaler = Scheduler(queueArrival = queues[0], randomNums = numbers, queueExit = queues[1])
    scaler.start(factory.getArrivalTime())*/
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