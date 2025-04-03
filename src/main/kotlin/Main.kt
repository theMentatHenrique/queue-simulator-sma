package br.com.pucrs

fun main() {
    var numbers = RandomNumbersManager.generateAndSave("random_numbers.txt").toMutableList()
    //numbers = mutableListOf(0.1,0.7,0.7,0.8,0.8,0.9)
    // Exercicio 2 do sor
    //val queue = Queue(capacity = 5, arrivalTimeA = 2.0, arrivalTimeB = 5.0, timeServiceA = 3.0, timeServiceB = 5.0, servers = 2)
    val queue = Queue(capacity = 5, arrivalTimeA = 2.0, arrivalTimeB = 5.0, timeServiceA = 3.0, timeServiceB = 5.0, servers = 2)

    // exemplo E1 Henrique
    //val queue = Queue(capacity = 4, arrivalTimeA = 2.0, arrivalTimeB = 3.0, timeServiceA = 1.0, timeServiceB = 2.0)

    // exemplo slides aula
    //numbers =  mutableListOf(0.3276, 0.8851, 0.1643, 0.5542, 0.6813, 0.7221, 0.9881)
    //val queue = Queue(capacity = 3, arrivalTimeA = 1.0, arrivalTimeB = 2.0, timeServiceA = 3.0, timeServiceB = 6.0)

    val scaler = scheduler(queue = queue, randomNums = numbers)
    scaler.init(42.0)
}

interface Event {
    fun getSingleEventTime() : Double
    fun getCurrentEventTime() : Double
    fun isArrival() : Boolean
}