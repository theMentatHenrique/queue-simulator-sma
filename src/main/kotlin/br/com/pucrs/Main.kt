package br.com.pucrs

import br.com.pucrs.factory.RandomNumbersManager

fun main() {

    // Fila 1 - G/G/2/3
    val queue1 = QueueImp(
        capacity = 3, 
        servers = 2, 
        arrivalTimeRange = Pair(1f, 4f),  
        serviceTimeRange = Pair(3f, 4f)  
    )

    // Fila 2 - G/G/1/5
    val queue2 = QueueImp(
        capacity = 5, 
        servers = 1,  
        arrivalTimeRange = Pair(2f, 3f), 
        serviceTimeRange = Pair(2f, 3f)
    )

    val network = QueueNetwork(queue1, queue2)

    network.runSimulation()
}

