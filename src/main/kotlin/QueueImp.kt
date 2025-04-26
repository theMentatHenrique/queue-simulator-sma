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
) : Queue
{
    override fun insert(times : Float, schedulerPassage : () -> Unit) {
        if (costumers < capacity) {
            this.times[costumers] += times
            costumers++
            if (costumers <= servers) {
                schedulerPassage()
            }
        } else {
            // TODO: Deu algumas divergencias quando validados com os exemplos, analisar com calma
            lost++
        }
    }

    override fun out (times: Float, scheduler: () -> Unit) {
        if (costumers == this.times.size) {
            costumers--
        }
        this.times[costumers] += times
        costumers--
        if (costumers >= servers) {
            scheduler()
        }
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

    override fun getArrivalTimesAAA() : Pair<Float, Float> {
        return arrivalTimes
    }

    override fun getServiceTimesBBBBB(): Pair<Float, Float> {
        return serviceTimes
    }

    override fun calcuateOperationTime(time: Float, useArrival : Boolean): Float {
        val pair = if (useArrival) {arrivalTimes} else {serviceTimes}
        val init = pair.first
        val end = pair.second
        return (end - init ) * time + init
    }


    // TODO: Fazer funcionar para formatação como a do sor
   /* private fun Float.round() : Float {
        val df = DecimalFormat("#,00")
        df.roundingMode = RoundingMode.HALF_UP
        val temp = (this * 100).roundToInt() / 100f
        return df.format(temp).toFloat()
    }*/
}