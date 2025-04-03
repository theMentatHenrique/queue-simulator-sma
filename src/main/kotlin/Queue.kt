package br.com.pucrs

import br.com.pucrs.domain.Queue
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class QueueImp(
    val servers: Int = 1,
    val capacity: Int = 0, // k
    val arrivalTimeA: Float = 0f,
    val arrivalTimeB: Float = 0f,
    val serviceTimeA: Float = 0f,
    val serviceTimeB: Float = 0f,
    private var status: Int = 0,
    private var lost: Int = 0,
    val times: MutableList<Float> = MutableList(capacity + 1) { 0f}
) : Queue
{
    override fun insert(time : Float, scheduler : () -> Unit) {
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

    override fun out (times: Float, schedulerExit: () -> Unit) {
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

    override fun getArrivalTimes() : Pair<Float, Float> {
        return Pair(arrivalTimeA, arrivalTimeB)
    }

    override fun getServiceTimes() : Pair<Float, Float> {
        return Pair(serviceTimeA, serviceTimeB)
    }

    // TODO: Fazer funcionar para formatação como a do sor
   /* private fun Float.round() : Float {
        val df = DecimalFormat("#,00")
        df.roundingMode = RoundingMode.HALF_UP
        val temp = (this * 100).roundToInt() / 100f
        return df.format(temp).toFloat()
    }*/
}