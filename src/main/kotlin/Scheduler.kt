package br.com.pucrs

import br.com.pucrs.domain.Queue
import br.com.pucrs.factory.EventFactory

class Scheduler(
    var randomNums : MutableList<Float> = mutableListOf(),
    private val queueArrival : Queue,
    private val queueExit : Queue,
    private var events : MutableList<Event> = mutableListOf(),
    private var totalTime : Float = 0f
) {
    fun init(seed : Float) {
        events.add(EventFactory.createEvent(singleTime = seed, currentTime = seed, "arrival" ))
        stagger()
        queueArrival.print()
        queueExit.print()
    }

     private fun stagger() {
         while(randomNums.isNotEmpty()) {
             val event = getNextEvent()
             if (event.isArrival()) {
                 arrival(event)
             } else if (event.isPassage()) {
                 passage(event)
             } else {
                 exit(event)
             }
             events.remove(event)
         }
     }

    private fun arrival(event: Event) {
        val auxTime = totalTime
        accumulateTime(event)
        var time = event.getCurrentEventTime() - auxTime

        if (queueArrival.status() < queueArrival.capacity()) {
            queueArrival.increment(time)
            if (queueArrival.status() <= queueArrival.servers()) {
                if (randomNums.isNotEmpty()) {
                    val time = queueArrival.calcuateOperationTime(randomNums.removeAt(0), false)
                    events.add(
                        EventFactory.createEvent(
                            type = "passage",
                            singleTime = time,
                            currentTime = totalTime + time
                        )
                    )
                }
            }
        } else {
            queueArrival.loss()
        }

        if (randomNums.isNotEmpty()) {
            time = queueArrival.calcuateOperationTime(randomNums.removeAt(0), true)
            events.add(
                EventFactory.createEvent(
                    type = "arrival",
                    singleTime = time,
                    currentTime = totalTime + time
                )
            )
        }
    }

    private fun exit(event : Event) {
        val auxTime = totalTime
        accumulateTime(event)
        queueExit.out()
        if (queueExit.status() >= queueExit.servers()) {
            val time = queueExit.calcuateOperationTime(randomNums.removeAt(0), false)
            events.add(EventFactory.createEvent(type = "exit", singleTime = time, currentTime = totalTime + time ))
        }
    }

    private fun passage(event : Event) {
        val auxTime = totalTime
        accumulateTime(event)
        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers()) {
            val time = queueArrival.calcuateOperationTime(randomNums.removeAt(0), false)
            events.add(EventFactory.createEvent(type = "passage", singleTime = time, currentTime = totalTime + time ))
        }

        if (queueExit.status() < queueExit.capacity()) {
            queueExit .increment(auxTime)
            if (queueExit.status() < queueExit.servers()) {
                val time = queueExit.calcuateOperationTime(randomNums.removeAt(0), false)
                events.add(EventFactory.createEvent(type = "exit", singleTime = time, currentTime = totalTime + time ))
            }
        } else {
            queueExit.loss()
        }
    }

    private fun accumulateTime(event: Event) {
        totalTime = event.getCurrentEventTime()
    }

    private fun getNextEvent(): Event {
        return events.minByOrNull {it.getCurrentEventTime()}!!
    }
 }