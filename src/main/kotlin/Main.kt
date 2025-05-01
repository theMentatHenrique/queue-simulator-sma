package br.com.pucrs

import br.com.pucrs.factory.RandomNumbersManager

fun main() {
    var numbers = RandomNumbersManager.generateAndSave("random_numbers.txt").toMutableList()
    val queueArrival = QueueImp(capacity = 3, arrivalTimes = Pair(1f, 4f), serviceTimes = Pair(3f, 4f), servers = 2)

    // FIla de saída n deve ter chegada, não sei como ficara com código assim
    val queueExit = QueueImp(capacity = 5, arrivalTimes = Pair(0f, 0f), serviceTimes = Pair(2f, 3f), servers = 1)
    val scaler = Scheduler(queueArrival = queueArrival, randomNums = numbers, queueExit = queueExit)
    scaler.start(2f)
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