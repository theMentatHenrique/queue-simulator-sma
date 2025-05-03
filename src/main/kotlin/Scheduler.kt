package br.com.pucrs

import br.com.pucrs.domain.Event
import br.com.pucrs.domain.Queue
import br.com.pucrs.factory.EventFactory
import kotlin.random.Random

class Scheduler(
    var randomNums : MutableList<Float> = mutableListOf(),
    private var events : MutableList<Event> = mutableListOf(),
    private val queues : List<Queue>,
    private var TG : Float = 0f,
) {
     lateinit var seed : Random

    fun start(startTime: Float) {
        seed = Random
        createEvent(
            "arrival",
            startTime,
            "Q1",
            "Q1"
        )
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
        queues.forEach{
            println("*************************************")
            it.print()
        }
    }

    private fun getQueueById(id : String) : Queue {
        return queues.first() { it.queueId() == id }
    }

    private fun arrival(event: Event) {
        accumulateTime(event)
        val queueArrivalId = event.queueArrival()
        val queueArrival = getQueueById(queueArrivalId)

        if (queueArrival.isInfinity() || queueArrival.status() < queueArrival.capacity()) {
            queueArrival.increment()
            if (queueArrival.status() <= queueArrival.servers() && randomNums.isNotEmpty()) {
               createEventByNextQueue(queueArrival)
            }
        } else {
            queueArrival.loss()
        }

        if(randomNums.isNotEmpty()) {
            createEvent(
                "arrival",
                TG + queueArrival.calcuateArrivalTime(randomNums.removeAt(0)),
                queueArrivalId,
                queueArrivalId
            )
        }
    }

    private fun exitSystem(event : Event) {
        accumulateTime(event)
        // a fila que chega é a que esta tratando a saída
        val queueExit = getQueueById(event.queueArrival())
        queueExit.out()
        if (queueExit.status() >= queueExit.servers() && randomNums.isNotEmpty()) {
            createEvent(
                "exitSystem",
                TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)),
                queueExit.queueId(),
                queueExit.queueId()
            )
        }
    }

    private fun exitQueue(ev : Event) {
        accumulateTime(ev)
        val queueArrival = getQueueById(ev.queueArrival())
        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers() && randomNums.isNotEmpty()) {
            createEventByNextQueue(queueArrival)
        }
    }

    private fun passage(event : Event) {
        accumulateTime(event)
        val queueArrival = getQueueById(event.queueArrival())
        val queueExit = getQueueById(event.queueExit())

        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers() && randomNums.isNotEmpty()) {
           createEventByNextQueue(queueArrival)
        }

        if (queueExit.isInfinity() || queueExit.status() < queueExit.capacity()) {
            queueExit.increment()
            if (queueExit.status() <= queueExit.servers() && randomNums.isNotEmpty()) {
                val random = seed.nextFloat()
                val nextQueue = queueExit.nextQueue(random)
                nextQueue?.let {
                    createEvent(
                        type = "passage",
                        TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)),
                        queueArrival = queueExit.queueId(),
                        nextQueue
                    )
                } ?: run {
                    // Não sei lidar com essa saída ainda, qual id devemos enviar por aqui ???
                    createEvent(
                        "exitSystem",
                        TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)),
                        queueExit.queueId(),
                        queueExit.queueId()
                    )
                }
            }
        } else {
            queueExit.loss()
        }
    }

    private fun createEventByNextQueue(queueOrigin : Queue) {
        val random = seed.nextFloat()
        val nextQueue = queueOrigin.nextQueue(random)
        nextQueue?.let {
            createEvent(
                type = "passage",
                TG + queueOrigin.calcuateServiceTime(randomNums.removeAt(0)),
                queueArrival = queueOrigin.queueId(),
                nextQueue
            )
        } ?: run {
            // Não sei lidar com essa saída ainda, qual id devemos enviar por aqui ???
            createEvent(
                "exit",
                TG + queueOrigin.calcuateServiceTime(randomNums.removeAt(0)),
                queueOrigin.queueId(),
                queueOrigin.queueId()
            )
        }
    }

    private fun accumulateTime(event: Event) {
        queues.forEach {
            it.incrementTime(event.getCurrentEventTime() - TG)
        }
        TG = event.getCurrentEventTime()
    }

    private fun getNextEvent(): Event {
        return events.minByOrNull {it.getCurrentEventTime()}!!
    }

    private fun createEvent(type : String, time : Float, queueArrival : String, queueExit : String) {
        events.add(
            EventFactory.createEvent(
                type = type,
                currentTime = time,
                queueArrival = queueArrival,
                queueExit = queueExit
            )
        )
    }
 }