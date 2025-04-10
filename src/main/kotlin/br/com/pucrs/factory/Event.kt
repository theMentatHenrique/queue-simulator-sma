package br.com.pucrs.factory

interface Event {
    fun getSingleEventTime(): Float
    fun getCurrentEventTime(): Float
    fun isArrival(): Boolean
}