package br.com.pucrs

import br.com.pucrs.domain.Queue
import kotlin.random.Random

class QueueNetwork(
    private val queue1: Queue,
    private val queue2: Queue,
    private var totalTime: Float = 0f,
    private val randomNums: MutableList<Float> = mutableListOf()
) {
    init {
        val randomGenerator = Random(42)
        for (i in 1..200000) { 
            randomNums.add(randomGenerator.nextFloat())
        }
    }

    fun runSimulation() {
        var clientIndex = 0

        while (clientIndex < 100000) { 
            clientIndex++

            println("\n====================")
            println("Simulando o atendimento do cliente #$clientIndex")
            println("====================")

            val arrivalTime1 = calculateArrivalTime(queue1.getArrivalTimes())
            println("Chegada na Fila 1 - Tempo de chegada: $arrivalTime1")

            if (queue1 is QueueImp && queue1.status < queue1.capacity) {
                queue1.insert(arrivalTime1) {
                    val serviceTime1 = calculateServiceTime(queue1.getServiceTimes())
                    println("Saindo da Fila 1 - Tempo de serviço: $serviceTime1")
                    queue1.out(serviceTime1) {

                        val serviceTime2 = calculateServiceTime(queue2.getServiceTimes())
                        println("Movendo para Fila 2 - Tempo de serviço: $serviceTime2")

                        if (queue2 is QueueImp && queue2.status < queue2.capacity) {
                            queue2.insert(serviceTime2) {
                                println("Cliente atendido na Fila 2 - Tempo de serviço: $serviceTime2")
                                queue2.out(serviceTime2, {})


                                println("\nEstado final após atendimento do cliente $clientIndex:")
                                queue2.print()
                            }
                        } else {
                            println("Cliente perdido na Fila 2 - Fila cheia!")
                        }
                    }
                }
            } else {
                println("Cliente perdido na Fila 1 - Fila cheia!")
            }

            println("\nEstado final da Fila 1 após atendimento do cliente $clientIndex:")
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
