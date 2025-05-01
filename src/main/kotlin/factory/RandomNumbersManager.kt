package br.com.pucrs.factory
import java.io.FileWriter
import kotlin.random.Random

object RandomNumbersManager {

    fun generateNumbersByLimit(limit: Int, seed: Int) : List<Float>{
       val seed = Random(seed)
        val randoms = mutableListOf<Float>()
        for (i in 0..limit) {
            randoms.add(seed.nextFloat())
        }
        return randoms;
    }

}