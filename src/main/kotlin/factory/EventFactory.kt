package br.com.pucrs.factory

import br.com.pucrs.Event

object EventFactory {
    fun createEvent(singleTime : Float, currentTime : Float, type : String) : Event {
        return object : Event {
            override fun getSingleEventTime(): Float {
                return singleTime
            }
            override fun getCurrentEventTime(): Float {
                return currentTime
            }
            override fun isArrival(): Boolean {
                return "arrival" == type
            }

            override fun isExit(): Boolean {
                return "exit" == type
            }

            override fun isPassage(): Boolean {
                return "passage" == type
            }
        }
    }
}