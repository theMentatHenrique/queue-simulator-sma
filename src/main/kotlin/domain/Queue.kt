package br.com.pucrs.domain

interface Queue {
    fun insert (times : Double, schedulerExit : () -> Unit)
    fun out (times : Double, schedulerExit : () -> Unit)
    fun print()
    fun getArrivalTimes() : Pair<Double, Double>
    fun getServiceTimes() : Pair<Double, Double>
}