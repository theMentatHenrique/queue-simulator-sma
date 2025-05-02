package br.com.pucrs

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
        seed = Random(2)
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
            it.print()
        }
    }

    private fun getQueueById(id : String) : Queue {
        return queues.first() { it.queueId() == id }
    }

    private fun arrival(event: Event) {
        val queueArrivalId = event.queueArrival()

        val queueArrival = getQueueById(queueArrivalId)
        accumulateTime(event)
        if (queueArrival.status() < queueArrival.capacity()) {
            queueArrival.increment()
            if (queueArrival.status() <= queueArrival.servers() && randomNums.isNotEmpty()) {
                val random = seed.nextFloat()
                val nextQueue = queueArrival.nextQueue(random)
                nextQueue?.let {
                    createEvent(
                        type = "passage",
                        TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)),
                        queueArrivalId,
                        getQueueById(nextQueue).queueId()
                    )} ?: run {
                    // Não sei lidar com essa saída ainda, qual id devemos enviar por aqui ???
                    createEvent(
                        "exit",
                        TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)),
                        queueArrivalId,
                        queueArrivalId
                    )
                }
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
            val random = seed.nextFloat()
            val nextQueue = queueArrival.nextQueue(random)
            nextQueue?.let {
                createEvent(
                    type = "passage",
                    TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)),
                    queueArrival = queueArrival.queueId(),
                    getQueueById(nextQueue).queueId()
                )
            } ?: run {
                // Não sei lidar com essa saída ainda, qual id devemos enviar por aqui ???
                createEvent(
                    "exit",
                    TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)),
                    queueArrival.queueId(),
                    queueArrival.queueId()
                )
            }

        }

    }

    private fun passage(event : Event) {
        accumulateTime(event)
        val queueArrival = getQueueById(event.queueArrival())
        val queueExit = getQueueById(event.queueExit())

        queueArrival.out()
        if (queueArrival.status() >= queueArrival.servers() && randomNums.isNotEmpty()) {
            val random = seed.nextFloat()
            val nextQueue = queueArrival.nextQueue(random)
            nextQueue?.let {
                createEvent(
                    type = "passage",
                    TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)),
                    queueArrival = queueArrival.queueId(),
                    getQueueById(nextQueue).queueId()
                )
            } ?: run {
                // Não sei lidar com essa saída ainda, qual id devemos enviar por aqui ???
                createEvent(
                    "exit",
                    TG + queueArrival.calcuateServiceTime(randomNums.removeAt(0)),
                    queueArrival.queueId(),
                    queueArrival.queueId()
                )
            }
        }

        queueExit.increment()
        if (queueExit.status() <= queueExit.servers() && randomNums.isNotEmpty()) {
            createEvent(
                "exitSystem",
                TG + queueExit.calcuateServiceTime(randomNums.removeAt(0)),
                queueExit.queueId(),
                queueExit.queueId()
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