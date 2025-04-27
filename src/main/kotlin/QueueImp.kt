package br.com.pucrs

import br.com.pucrs.domain.Queue

class QueueImp(
    val servers: Int = 1,
    val capacity: Int = 0, // k
    val arrivalTimes: Pair<Float, Float> = 0f to 0f,
    val serviceTimes: Pair<Float, Float> = 0f to 0f,
    private var costumers: Int = 0,
    private var lost: Int = 0,
    private val times: MutableList<Float> = MutableList(capacity + 1) { 0f}
) : Queue {


    override fun out(times: Float) {
        if (status() == this.times.size) {
            costumers--
        }
        this.times[costumers] += times
        costumers--
    }

    override fun print() {
        var iterator = 0f
        var total = 0f;
        var percent = 0f
        times.forEach {
            total+=it
        }
        println("Estado da fila |  Tempo(segundos) | Probabilidade")
        times.forEach {
            val percAux  = (it/total) * 100f
            println("${iterator}  | ${it}  | ${percAux}%")
            iterator++
            percent += percAux
        }
        println("Total | ${total} | ${percent}%")
        println("Perdido:${lost}")
    }

    override fun calcuateOperationTime(time: Float, useArrival : Boolean): Float {
        val pair = if (useArrival) {arrivalTimes} else {serviceTimes}
        val init = pair.first
        val end = pair.second
        return (end - init ) * time + init
    }

    override fun status() : Int {
        return costumers
    }

    override fun servers(): Int {
        return servers
    }

    override fun increment(time : Float) {
        this.times[costumers] += time
        costumers++
    }

    override fun loss() {
        lost++
    }

    override fun capacity(): Int {
        return capacity
    }
}