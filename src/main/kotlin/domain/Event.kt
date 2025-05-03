package br.com.pucrs.domain

interface Event {
    fun getCurrentEventTime() : Float
    fun isArrival() : Boolean
    fun isPassage() : Boolean
    fun isExitQueue() : Boolean
    fun isExitSystem() : Boolean
    fun queueArrival() : String
    fun queueExit() : String
}