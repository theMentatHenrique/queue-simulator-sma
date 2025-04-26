package br.com.pucrs.factory
import java.io.FileWriter

object RandomNumbersManager {
    fun generateRandomNumbers(size: Int = 1000, seed: Long, a: Long, c: Long, m: Long): List<Float> {
        val numbers = mutableListOf(seed.toFloat())
        var actual = seed.toDouble()
        for (i in 1 until size) {
            actual = (a * actual + c) % m
            numbers.add((actual / m).toFloat())
        }
        return numbers
    }
}