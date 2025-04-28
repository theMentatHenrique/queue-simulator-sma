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
    fun calcuateOperationTime(time : Float, useArival : Boolean) : Float
}