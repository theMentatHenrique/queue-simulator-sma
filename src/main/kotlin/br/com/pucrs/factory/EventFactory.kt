package br.com.pucrs.factory

import br.com.pucrs.factory.Event

object EventFactory {
    fun createEvent(singleTime: Float, currentTime: Float, isArrival: Boolean): Event {
        return object : Event {
            override fun getSingleEventTime(): Float {
                return singleTime
            }
            override fun getCurrentEventTime(): Float {
                return currentTime
            }
            override fun isArrival(): Boolean {
                return isArrival
            }
        }
    }
}