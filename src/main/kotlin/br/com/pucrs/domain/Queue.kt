package br.com.pucrs.domain



interface Queue {
    val capacity: Int
    val servers: Int

    fun insert(time: Float, schedulerExit: () -> Unit)
    fun out(time: Float, schedulerExit: () -> Unit)
    fun print()
    fun getArrivalTimes(): Pair<Float, Float>
    fun getServiceTimes(): Pair<Float, Float>
}