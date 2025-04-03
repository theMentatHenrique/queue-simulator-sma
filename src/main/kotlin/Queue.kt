package br.com.pucrs

import br.com.pucrs.domain.Queue

class QueueImp(
    val servers: Int = 1,
    val capacity: Int = 0, // k
    val arrivalTimeA: Double = 0.0,
    val arrivalTimeB: Double = 0.0,
    val serviceTimeA: Double = 0.0,
    val serviceTimeB: Double = 0.0,
    private var status: Int = 0,
    private var lost: Int = 0,
    val times: MutableList<Double> = MutableList(capacity + 1) { 0.0 }
) : Queue
{
    override fun insert(time : Double, scheduler : () -> Unit) {
        if (status < capacity) {
            times[status] += time
            status++
            if (status <= servers) {
                scheduler()
            }
        } else {
            // TODO: Deu algumas divergencias quando validados com os exemplos, analisar com calma
            lost++
        }
    }

    override fun out (times: Double, schedulerExit: () -> Unit) {
        if (status == this.times.size) {
            status--
        }
        this.times[status] += times
        status--
        if (status >= servers) {
            schedulerExit()
        }
    }

    override fun print() {
        var iterator = 0
        var total = 0.0;
        var percent = 0.0
        times.forEach {
            total+=it
        }
        println("Estado da fila |  Tempo(segundos) | Probabilidade")
        times.forEach {
            val percAux  = (it/total) * 100.0
            println("${iterator}  | ${it}  | ${percAux}%")
            iterator++
            percent+=percAux
        }
        println("Total | ${total} | ${percent}%")
        println("Perdido:${lost}")
    }

    override fun getArrivalTimes() : Pair<Double, Double> {
        return Pair(arrivalTimeA, arrivalTimeB)
    }

    override fun getServiceTimes() : Pair<Double, Double> {
        return Pair(serviceTimeA, serviceTimeB)
    }
}