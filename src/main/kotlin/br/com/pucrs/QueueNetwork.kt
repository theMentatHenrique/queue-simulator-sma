package br.com.pucrs

import br.com.pucrs.domain.Queue

class QueueNetwork(
    private val queue1: Queue,
    private val queue2: Queue,
    private var totalTime: Float = 0f,
    private val randomNums: MutableList<Float> = mutableListOf()
) {
    fun runSimulation() {
        for (clientIndex in 1..10) {
            println("Simulando o atendimento do cliente $clientIndex")

            val arrivalTime1 = calculateArrivalTime(queue1.getArrivalTimes())
            println("Cliente $clientIndex chega na Fila 1 - Tempo de chegada: $arrivalTime1")

            if (queue1 is QueueImp && queue1.status < queue1.capacity) {
                queue1.insert(arrivalTime1) {
                    val serviceTime1 = calculateServiceTime(queue1.getServiceTimes())
                    println("Cliente $clientIndex sai de Fila 1 - Tempo de serviço: $serviceTime1")
                    queue1.out(serviceTime1) {

                        val serviceTime2 = calculateServiceTime(queue2.getServiceTimes())
                        println("Cliente $clientIndex movido para Fila 2 - Tempo de serviço: $serviceTime2")

                        if (queue2 is QueueImp && queue2.status < queue2.capacity) {
                            queue2.insert(serviceTime2) {
                                println("Cliente $clientIndex atendido na Fila 2 - Tempo de serviço: $serviceTime2")
                                queue2.out(serviceTime2, {}) 

                                println("Estado final da Fila 2 após atendimento do cliente $clientIndex:")
                                queue2.print()
                            }
                        } else {
                            println("Cliente $clientIndex perdido - Fila 2 cheia!")
                        }
                    }
                }
            } else {
                println("Cliente $clientIndex perdido - Fila 1 cheia!")
            }

            println("Estado final da Fila 1 após atendimento do cliente $clientIndex:")
            queue1.print()
        }
    }

    private fun calculateArrivalTime(range: Pair<Float, Float>): Float {
        val arrivalTime = range.first + (range.second - range.first) * randomNums.removeAt(0)
        println("Calculando tempo de chegada: $arrivalTime")
        return arrivalTime
    }

    private fun calculateServiceTime(range: Pair<Float, Float>): Float {
        val serviceTime = range.first + (range.second - range.first) * randomNums.removeAt(0)
        println("Calculando tempo de serviço: $serviceTime")
        return serviceTime
    }
}
