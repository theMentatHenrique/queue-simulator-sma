package br.com.pucrs

import br.com.pucrs.domain.Queue

class QueueImp(
    override val capacity: Int,
    override val servers: Int,
    val arrivalTimeRange: Pair<Float, Float>,
    val serviceTimeRange: Pair<Float, Float>
) : Queue {
    var status = 0  // Este campo controla quantos clientes estão na fila.
    private var lost = 0
    private val times = MutableList(capacity + 1) { 0f }

    override fun insert(time: Float, scheduler: () -> Unit) {
        println("Inserindo na fila - Tempo: $time, Status atual: $status")
        if (status < capacity) {
            times[status] += time
            println("Fila atualizada - Novo tempo na fila: ${times[status]}")
            status++
            if (status <= servers) {
                scheduler()
            }
        } else {
            lost++
            println("Cliente perdido! Fila cheia.")
        }
    }

    override fun out(time: Float, schedulerExit: () -> Unit) {
        println("Saindo da fila - Tempo: $time, Status atual: $status")
        if (status > 0) {
            times[status] += time
            println("Fila atualizada após saída - Novo tempo na fila: ${times[status]}")
            status--
            if (status >= servers) {
                schedulerExit()
            }
        }
    }

    override fun print() {
        var total = 0f
        times.forEach { total += it }

        // Verificando o total de tempos acumulados antes de calcular a probabilidade
        println("Total de tempo acumulado nas filas: $total")

        // Se o total for zero, não faz sentido calcular as probabilidades
        if (total == 0f) {
            println("Nenhum tempo acumulado, todas as probabilidades serão 0%.")
        }

        println("Fila Estado | Tempo | Probabilidade")
        times.forEachIndexed { index, time ->
            val probability = if (total > 0f) (time / total) * 100 else 0f
            println("Posição $index | $time | $probability%")
        }

        println("Perdidos: $lost")
    }

    override fun getArrivalTimes(): Pair<Float, Float> = arrivalTimeRange
    override fun getServiceTimes(): Pair<Float, Float> = serviceTimeRange
}
