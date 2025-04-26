package br.com.pucrs.domain

interface Queue {
    fun insert (times : Float, schedulerPassage : () -> Unit)
    fun out (times : Float, scheduler : () -> Unit)
    fun print()
    fun getArrivalTimesAAA() : Pair<Float, Float>
    fun getServiceTimesBBBBB() : Pair<Float, Float>
    fun calcuateOperationTime(time : Float, useArival : Boolean) : Float
}