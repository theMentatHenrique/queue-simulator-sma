package br.com.pucrs.factory

import br.com.pucrs.Event

object EventFactory {
    fun createEvent(currentTime : Float, type : String, queueArrival : String, queueExit : String) : Event {
        return object : Event {
            override fun getCurrentEventTime(): Float {
                return currentTime
            }
            override fun isArrival(): Boolean {
                return "arrival" == type
            }

            override fun isExitQueue(): Boolean {
               return "exit" == type
            }

            override fun isExitSystem(): Boolean {
                return "exitSystem" == type
            }

            override fun isPassage(): Boolean {
                return "passage" == type
            }

            override fun queueArrival(): String {
                return queueArrival
            }

            override fun queueExit(): String {
               return queueExit
            }
        }
    }
}