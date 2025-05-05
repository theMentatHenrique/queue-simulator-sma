package br.com.pucrs

import br.com.pucrs.domain.Queue

class QueueImp(
    val id : String,
    val servers: Int = 0,
    val capacity: Int = 0, // k
    val arrivalTimes: Pair<Float, Float> = 0f to 0f,
    val serviceTimes: Pair<Float, Float> = 0f to 0f,
    var queues: MutableMap<Float, String> = mutableMapOf() // Inicializado como MutableMap

) : Queue {
    private var costumers: Int = 0
    private var lost: Int = 0
    private val times: MutableList<Float> = MutableList(100) { 0f}

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
        if (costumers >= times.size) return;
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

    override fun nextQueue(prob: Float): String? {
        if (queues.isEmpty()) return null
        var sum = queues.keys.first()
        for (it in queues) {
            if (prob <= sum) {
                return it.value
            } else {
                sum += it.key
            }
        }
        return null;
    }


    override fun queueId() : String {
        return id
    }

    override fun isInfinity(): Boolean {
        return capacity == 0
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
            if (it != 0f) {
                val percAux  = (it/total) * 100f
                println("${iterator}  | ${it}  | ${percAux}%")
                iterator++
                percent += percAux
            }
        }
        println("Total | ${total} | ${percent}%")
        println("Perdido:${lost}")
    }
}