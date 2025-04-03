package Factory.domain

class Queue(
    var status: Int = 0,
    val servers: Int = 1,
    val capacity: Int = 0, // k
    val arrivalTimeA: Double = 0.0,
    val arrivalTimeB: Double = 0.0,
    val timeServiceA: Double = 0.0,
    val timeServiceB: Double = 0.0,
    var lost: Int = 0,
    val times: MutableList<Double> = MutableList(capacity + 1) { 0.0 })
{
    fun printQueue() {
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
}