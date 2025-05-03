package br.com.pucrs

import br.com.pucrs.factory.QueueFactory
import br.com.pucrs.factory.RandomNumbersManager
import java.io.File

fun main() {

    var factory = QueueFactory()
    val queues = factory.readQueuesAndNetworksFromYaml(getAbsolutePath("model.yml"))
    val numbers = RandomNumbersManager.generateNumbersByLimit(limit = factory.getrandom(), factory.getArrivalTime().toInt())
    val scaler = Scheduler(queues = queues, randomNums = numbers.toMutableList())
    scaler.start(factory.getArrivalTime())
}

fun getAbsolutePath(filePath : String) : String {
    val arquivo = File(filePath)
    val caminhoAbsoluto = arquivo.absolutePath
    var split = caminhoAbsoluto.removeSuffix("/$filePath")
    split+= "/build/resources/main/$filePath"
    return split
}
