package br.com.pucrs

import br.com.pucrs.domain.Queue
import kotlin.math.cos

class QueueImp(
    val servers: Int = 1,
    val capacity: Int = 0, // k
    val arrivalTimes: Pair<Float, Float> = 0f to 0f,
    val serviceTimes: Pair<Float, Float> = 0f to 0f,
    private var costumers: Int = 0,
    private var lost: Int = 0,
    private val times: MutableList<Float> = MutableList(capacity + 1) { 0f}
) : Queue {
    override fun out() {
        costumers--
    }

    override fun status() : Int {
        return costumers
    }

    override fun servers(): Int {
        return servers
    }

    override fun increment() {
        costumers++
    }

    override fun incrementTime(time: Float) {
        // TODO: validar este caso
        if (costumers == times.size) return;
        this.times[costumers] += time
    }

    override fun loss() {
        lost++
    }

    private fun calcuateOperationTime(time: Float, pair : Pair<Float, Float>): Float {
        val init = pair.first
        val end = pair.second
        return (end - init) * time + init
    }

    override fun calcuateServiceTime(time: Float): Float {
        return calcuateOperationTime(time, serviceTimes)
    }

    override fun calcuateArrivalTime(time: Float): Float {
        return calcuateOperationTime(time, arrivalTimes)
    }

    override fun capacity(): Int {
        return capacity
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
}