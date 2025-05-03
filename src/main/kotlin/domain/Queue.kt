package br.com.pucrs.domain

interface Queue {
    fun print()
    fun status() : Int
    fun servers() : Int
    fun increment()
    fun incrementTime(time : Float)
    fun capacity() : Int
    fun out()
    fun loss()
    fun calcuateServiceTime(time : Float) : Float
    fun calcuateArrivalTime(time : Float) : Float
    fun nextQueue(prob : Float) : String?
    fun queueId() : String
    fun isInfinity() : Boolean
}