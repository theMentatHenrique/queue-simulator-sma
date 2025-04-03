package br.com.pucrs.domain

import br.com.pucrs.Event

object EventFactory {
    fun createEvent(singleTime : Double, currentTime : Double, isArrival : Boolean) : Event {
        return object : Event {
            override fun getSingleEventTime(): Double {
                return singleTime
            }
            override fun getCurrentEventTime(): Double {
                return currentTime
            }
            override fun isArrival(): Boolean {
                return isArrival
            }
        }
    }
}