package br.com.pucrs


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
        val time = event.getCurrentEventTime() - auxTime
        queue.insert(time) {schedulerExit()}
         schedulerArrival()
     }

    private fun accumulateTime(event: Event) {
        totalTime = event.getCurrentEventTime()
    }

    private fun exit(event : Event) {
        val auxTime = totalTime
        accumulateTime(event)
        queue.out(event.getCurrentEventTime() , auxTime) { schedulerExit() }
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