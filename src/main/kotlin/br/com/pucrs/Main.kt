package br.com.pucrs

import br.com.pucrs.factory.RandomNumbersManager

fun main() {
    val randomNums = RandomNumbersManager.generateRandomNumbers(100000, 42, 3432432, 98753, 354325325342534).toMutableList()

    val queue1 = QueueImp(
        capacity = 3,
        servers = 2,
        arrivalTimeRange = Pair(1f, 4f),
        serviceTimeRange = Pair(3f, 4f)
    )

    val queue2 = QueueImp(
        capacity = 5,
        servers = 1,
        arrivalTimeRange = Pair(2f, 3f),
        serviceTimeRange = Pair(2f, 3f)
    )

    val network = QueueNetwork(queue1, queue2, randomNums = randomNums)

    network.runSimulation()
}
