package br.com.pucrs.factory

import br.com.pucrs.QueueImp
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

class QueueFactory {
    private var arrivalTime : Float = 0.0f

    fun readQueuesAndNetworksFromYaml(filePath: String): List<QueueImp> {
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule.Builder().build())
        val yamlData: Map<String, Any> = mapper.readValue(File(filePath), Map::class.java) as Map<String, Any>

        val queuesData = yamlData["queues"] as? Map<String, Map<String, Any>> ?: emptyMap()
        val networkData = yamlData["network"] as? List<Map<String, Any>> ?: emptyList()

        val queuesMap = mutableMapOf<String, QueueImp>()
        queuesData.forEach { (queueId, queueConfig) ->
            val servers = (queueConfig["servers"] as? Int) ?: 1
            val capacity = (queueConfig["capacity"] as? Int) ?: 0
            val minArrival = (queueConfig["minArrival"] as? Double)?.toFloat() ?: 0f
            val maxArrival = (queueConfig["maxArrival"] as? Double)?.toFloat() ?: 0f
            val minService = (queueConfig["minService"] as? Double)?.toFloat() ?: 0f
            val maxService = (queueConfig["maxService"] as? Double)?.toFloat() ?: 0f

            val queue = QueueImp(
                id = queueId,
                servers = servers,
                capacity = capacity,
                arrivalTimes = minArrival to maxArrival,
                serviceTimes = minService to maxService
            )
            queuesMap[queueId] = queue
        }

        // Processar a seção 'network' e popular o mapa 'queues' dos objetos QueueImp
        networkData.forEach { connectionConfig ->
            val source = connectionConfig["source"] as? String
            val target = connectionConfig["target"] as? String
            val probability = (connectionConfig["probability"] as? Double)?.toFloat()

            if (source != null && target != null && probability != null && queuesMap.containsKey(source)) {
                queuesMap[source]?.queues?.put(probability.toFloat(), target)
            }
        }

        return queuesMap.values.toList()
    }

    fun getArrivalTime() : Float {
        return 2f
    }
}