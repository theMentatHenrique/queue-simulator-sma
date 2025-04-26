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
        val time = event.getCurrentEventTime() - auxTime
        queueArrival.insert(time) {schedulerPassage()}
        schedulerArrival()
     }

    private fun accumulateTime(event: Event) {
        totalTime = event.getCurrentEventTime()
    }

    private fun exit(event : Event) {
        val auxTime = totalTime
        accumulateTime(event)
        queueExit.out(event.getCurrentEventTime() - auxTime) { schedulerExit() }
    }

    private fun passage(event : Event) {
        val auxTime = totalTime
        accumulateTime(event)
        // Os eventos manipulados abaixo devem ser diferentes, devo corrigir
        queueArrival.out(event.getCurrentEventTime() - auxTime) {schedulerPassage()}

        queueExit.insert(event.getCurrentEventTime() - auxTime) {schedulerExit()}
    }

     private fun schedulerExit() {
         if (randomNums.isEmpty()) return
         val time = queueArrival.calcuateOperationTime(randomNums.removeAt(0), false)
         queueArrival.calcuateOperationTime(randomNums.removeAt(0), false)
         events.add(EventFactory.createEvent(type = "exit", singleTime = time, currentTime = totalTime + time ))
     }

     private fun schedulerArrival() {
         if (randomNums.isEmpty()) return
         val time = queueArrival.calcuateOperationTime(randomNums.removeAt(0), true)

         events.add(EventFactory.createEvent(type = "arrival", singleTime = time, currentTime = totalTime + time ))
     }

    private fun schedulerPassage() {
        if (randomNums.isEmpty()) return
        val time = queueArrival.calcuateOperationTime(randomNums.removeAt(0), true)
        events.add(EventFactory.createEvent(type = "passage", singleTime = time, currentTime = totalTime + time ))
    }

     private fun getNextEvent(): Event {
         return events.minByOrNull {it.getCurrentEventTime()}!!
     }
 }