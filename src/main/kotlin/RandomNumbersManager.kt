package br.com.pucrs
import java.io.FileWriter

object RandomNumbersManager {
    fun generateAndSave(fileName : String) : List<Float> {
        val numbers = generateRandomNumbers(
            size = 100000,
            seed = 42,
            a = 3432432,
            c = 98753,
            m = 354325325342534
        )
        saveCSV(numbers, fileName)
        return numbers
    }


    private fun generateRandomNumbers(
        size: Int = 1000,
        seed: Long,
        a: Long,
        c: Long,
        m: Long
    ): List<Float> {
        val numbers = mutableListOf(seed.toFloat())
        var actual = seed.toDouble()
        for (i in 1 until size) {
            actual = (a * actual + c) % m
            numbers.add((actual / m).toFloat())
        }
        return numbers
    }

    private fun saveCSV(list: List<Float>, fileName: String) {
        val fileWriter = FileWriter(fileName)
        fileWriter.use { writer ->
            list.forEach { linha ->
                writer.append("- ${linha.toString()}")
                writer.append("\n")
            }
        }
    }
}