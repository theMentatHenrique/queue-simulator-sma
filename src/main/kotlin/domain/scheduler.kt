package Factory.domain

import br.com.pucrs.Event
import br.com.pucrs.domain.EventFactory


class scheduler(
    private val queue : Queue,
    private var events : MutableList<Event> = mutableListOf(),
    private var randomNums : MutableList<Double> = mutableListOf(),
    private var totalTime : Double = 0.0
) {
    fun init(seed : Double) {
        events.add(EventFactory.createEvent(singleTime = seed, currentTime = seed, true ))
        stagger()
        queue.printQueue()
    }

     private fun stagger() {
         while(randomNums.isNotEmpty()) {
             val event = getNextEvent()
             if (event.isArrival()) {
                 arrival(event)
             } else {
                 exit(event)
             }
             events.remove(event)
         }
     }

    private fun arrival(event: Event) {
        val auxTime = totalTime
        accumulateTime(event)
         if (queue.status < queue.capacity) {
             queue.times[queue.status] += event.getCurrentEventTime() - auxTime
             queue.status++
             if (queue.status <= queue.servers) {
                schedulerExit()
             }
         } else {
             // TODO: Deu algumas divergencias quando validados com os exemplos, analisar com calma
             queue.lost++
         }
         schedulerArrival()
     }

    private fun accumulateTime(event: Event) {
        totalTime = event.getCurrentEventTime()
    }

    private fun exit(event : Event) {
        try {
            val auxTime = totalTime
            accumulateTime(event)
            if (queue.status == queue.times.size) {
                queue.status--
            }
            queue.times[queue.status] += event.getCurrentEventTime() - auxTime
            queue.status--
            if (queue.status >= queue.servers) {
                schedulerExit()
            }
        } catch (e: Exception) {
            print("helolo")
        }
    }

     private fun schedulerExit() {
         if (randomNums.isEmpty()) return
         val time = calculateTimeByRandom(queue.timeServiceA, queue.timeServiceB)
         events.add(EventFactory.createEvent(isArrival = false, singleTime = time, currentTime = totalTime + time ))
     }

     private fun schedulerArrival() {
         if (randomNums.isEmpty()) return
         val time = calculateTimeByRandom(queue.arrivalTimeA, queue.arrivalTimeB)
         events.add(EventFactory.createEvent(isArrival = true, singleTime = time, currentTime = totalTime + time ))
     }

     private fun calculateTimeByRandom(init : Double, end: Double): Double {
         val random = randomNums.removeAt(0)
         return (end - init ) * random + init
     }

     private fun getNextEvent(): Event {
         return events.minByOrNull {it.getCurrentEventTime()}!!
     }
 }