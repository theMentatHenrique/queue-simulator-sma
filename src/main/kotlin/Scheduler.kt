package br.com.pucrs

import br.com.pucrs.domain.Queue
import br.com.pucrs.factory.EventFactory

class Scheduler(
    var randomNums : MutableList<Float> = mutableListOf(),
    private val queueArrival : Queue,
    private val queueExit : Queue,
    private var events : MutableList<Event> = mutableListOf(),
    private var TG : Float = 0f,
) {
      fun start(seed: Float) {
          events.add(EventFactory.createEvent(currentTime = seed, "arrival"))
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
            if (queueArrival.status() <= queueArrival.servers()) {
                createEvent("passage", TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)))
            }
        } else {
            queueArrival.loss()
        }
        createEvent("arrival", TG + queueArrival.calcuateArrivalTime(randomNums.removeAt(0)))
    }

    private fun exit(event : Event) {
        accumulateTime(event)
        queueExit.out()
        if (queueExit.status() >= queueExit.servers()) {
            createEvent("exit", TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)))
        }
    }

    private fun passage(event : Event) {
        accumulateTime(event)
        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers()) {
            createEvent("passage", TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)))
        }

        if (queueExit.status() < queueExit.capacity()) {
            queueExit.increment()
            if (queueExit.status() <= queueExit.servers()) {
                createEvent("exit", TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)))
            }
        } else {
            queueExit.loss()
        }
    }

    private fun accumulateTime(event: Event) {
        queueExit.incrementTime(event.getCurrentEventTime() - TG)
        queueArrival.incrementTime(event.getCurrentEventTime() - TG)
        TG = event.getCurrentEventTime()
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