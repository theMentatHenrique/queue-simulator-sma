package br.com.pucrs

import br.com.pucrs.domain.Queue
import br.com.pucrs.factory.EventFactory
import kotlin.random.Random

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
             } else if (event.isExitSystem()) {
                 exitSystem(event)
             } else if(event.isExitQueue()) {
                 exitQueue(event)
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
                createEvent(if (Random.nextFloat() < 0.7f) "passage" else "exit",
                    TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0))
                )
            }
        } else {
            queueArrival.loss()
        }
        createEvent("arrival", TG + queueArrival.calcuateArrivalTime(randomNums.removeAt(0)))
    }

    private fun exitSystem(event : Event) {
        accumulateTime(event)
        queueExit.out()
        if (queueExit.status() >= queueExit.servers()) {
            createEvent("exitSystem", TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)))
        }
    }

    private fun exitQueue(ev : Event) {
        accumulateTime(ev)
        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers()) {
            createEvent(type = if (Random.nextFloat() < 0.7f) "passage" else "exit",
                time = TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)))
        }

    }

    private fun passage(event : Event) {
        accumulateTime(event)
        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers()) {
            createEvent(if (Random.nextFloat() < 0.7f) "passage" else "exit",
                TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)))
        }

        queueExit.increment()
        if (queueExit.status() <= queueExit.servers()) {
            createEvent("exitSystem", TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)))
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