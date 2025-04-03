package br.com.pucrs.domain

interface Queue {
    fun insert (times : Float, schedulerExit : () -> Unit)
    fun out (times : Float, schedulerExit : () -> Unit)
    fun print()
    fun getArrivalTimes() : Pair<Float, Float>
    fun getServiceTimes() : Pair<Float, Float>
}