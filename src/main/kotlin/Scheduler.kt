package br.com.pucrs

import br.com.pucrs.domain.Queue
import br.com.pucrs.factory.EventFactory

class Scheduler(
    var randomNums : MutableList<Float> = mutableListOf(),
    private val queueArrival : Queue,
    private val queueExit : Queue,
    private var events : MutableList<Event> = mutableListOf(),
    private var totalTime : Float = 0f,
) {
    fun init(seed : Float) {
        events.add(EventFactory.createEvent(currentTime = seed, "arrival"))
    }

      fun start() {
         while(randomNums.isNotEmpty()) {
             val event = getNextEvent()
             if (event.isArrival()) {
                 arrival(event)
             } else if (event.isExit()) {
                 exit(event)
             } else {
                 passage(event)
             }
             events.remove(event)
         }
          queueArrival.print()
          queueExit.print()
     }

    private fun arrival(event: Event) {
        accumulateTime(event)
        if (queueArrival.status() < queueArrival.capacity()) {
            queueArrival.increment()
            if (queueArrival.status() <= queueArrival.servers() && randomNums.isNotEmpty()) {
                createEvent("passage", totalTime + queueArrival.calcuateServiceTime(randomNums.removeAt(0)))
            }
        } else {
            queueArrival.loss()
        }

        if (randomNums.isNotEmpty()) {
            createEvent("arrival", totalTime + queueArrival.calcuateArrivalTime(randomNums.removeAt(0)))
        }
    }

    private fun exit(event : Event) {
        accumulateTime(event)
        queueExit.out()
        if (queueExit.status() >= queueExit.servers()) {
            createEvent("exit", totalTime + queueExit.calcuateServiceTime(randomNums.removeAt(0)))
        }
    }

    private fun passage(event : Event) {
        accumulateTime(event)
        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers()) {
            createEvent("passage", totalTime + queueArrival.calcuateServiceTime(randomNums.removeAt(0)))
        }

        queueExit.increment()
        if (queueExit.status() < queueExit.capacity()) {
            if (queueExit.status() <= queueExit.servers()) {
                createEvent("exit", totalTime + queueExit.calcuateServiceTime(randomNums.removeAt(0)))
            }
        } else {
            queueExit.loss()
        }
    }

    private fun accumulateTime(event: Event) {
        queueExit.incrementTime(event.getCurrentEventTime() - totalTime)
        queueArrival.incrementTime(event.getCurrentEventTime() - totalTime)
        totalTime = event.getCurrentEventTime()
    }

    private fun getNextEvent(): Event {
        return events.minByOrNull {it.getCurrentEventTime()}!!
    }

    private fun createEvent(type : String, time : Float) {
        events.add(
            EventFactory.createEvent(
                type = type,
                currentTime = time
            )
        )
    }
 }