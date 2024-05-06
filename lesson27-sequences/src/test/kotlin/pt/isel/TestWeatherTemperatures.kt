package pt.isel

import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/*
 * Located on testes resources
 */
private const val weatherPath = "q-Lisbon_format-csv_date-2020-05-08_enddate-2020-06-11.csv"

class TestWeatherTemperatures {

    fun loadNaiveCsv(): List<Weather> {
        return ClassLoader
            .getSystemResource(weatherPath)
            .openStream()
            .reader()
            .readLines()
            .filter { !it.startsWith('#') } // Filter comments
            .drop(1) // Skip line: Not available
            .filterIndexed { index, _ ->  index % 2 != 0} // Filter hourly info
            .map { it.fromCsvToWeather() }
    }

    private val weatherData = loadNaiveCsv()

    @Test fun checkData() {
        weatherData
            .forEach { println(it) }
    }

    @Test fun weatherDescriptionsOnRainnyDaysMapFilter() {
        var count = 0
        val weatherDescs = weatherData
            .eagerMap { count++; it.weatherDesc }
            .eagerFilter { count++; it.lowercase().contains("rain") }
            .distinct()
        assertEquals(5, weatherDescs.size)
        assertEquals(70, count)
        weatherDescs.forEach { println(it) }

    }
    @Test fun weatherDescriptionsOnRainnyDaysFilterMap() {
        var count = 0
        val weatherDescs = weatherData
            .eagerFilter { count++; it.weatherDesc.lowercase().contains("rain") }
            .eagerMap { count++; it.weatherDesc }
            .distinct()
        assertEquals(5, weatherDescs.size)
        assertEquals(48, count)
        weatherDescs.forEach { println(it) }
    }
    @Test fun firstWeatherDescInWindyDaysEager() {
        var count = 0;
        val windyDesc = weatherData
            .filter { count++; it.windspeedKmph > 20 } // bigger than 20 Kms/h
            .map { count++; it.weatherDesc }
            .first()
        assertEquals("Light rain shower", windyDesc)
        assertEquals(39, count)
    }
    @Test fun firstWeatherDescInWindyDaysLazy() {
        var count = 0;
        val windyDesc = weatherData
            .asSequence()
            .lazyFilter { count++; it.windspeedKmph > 20 } // bigger than 20 Kms/h
            .lazyMap { count++; it.weatherDesc }
            .first()
        assertEquals(5, count)
        assertEquals("Light rain shower", windyDesc)
    }
    @Test fun distinctWeatherDescInWindyDaysEager() {
        var count = 0;
        val distinct = weatherData
            .filter { count++; it.windspeedKmph > 20 } // bigger than 20 Kms/h
            .distinct()
        assertEquals(35, count)
    }
    @Test fun distinctWeatherDescInWindyDaysLazy() {
        var count = 0;
        val distinct = weatherData
            .asSequence()
            .lazyFilter { count++; it.windspeedKmph > 20 } // bigger than 20 Kms/h
            .lazyDistinct()
        assertEquals(0, count) // NOTHING happens without a terminal operation
    }

    @Test fun infiniteSequence() {
        val nrs: Sequence<Int> = generateSequence {
            Random.nextInt(100)
        }
        nrs
            .take(10)
            .forEach { println(it) }
    }

    fun nrsGenerator(shortcut: Boolean = false) = sequence<Int> {
        println("Starting...")
        yield(7)
        println("Emit 2nd Element")
        yield(6)
        if(shortcut)
            return@sequence
        println("Emit 3rd Element")
        yield(5)
    }

    @Test fun testGenerator() {
        val nrs = nrsGenerator()
        val iter = nrs.iterator()
        println("Iterator invoked")
        println(iter.next())
        println(iter.next())
        println(iter.next())
    }

    @Test fun testGeneratorNoSuchElementExcetion() {
        val nrs = nrsGenerator(true)
        val iter = nrs.iterator()
        println("Iterator invoked")
        println(iter.next())
        println(iter.next())
        assertFailsWith<NoSuchElementException> {
            println(iter.next()) // Exception
        }
    }
    @Test fun firstWeatherDescInWindyDaysLazyWithYield() {
        var count = 0;
        val windyDesc = weatherData
            .asSequence()
            .yieldFilter  { count++; it.windspeedKmph > 20 } // bigger than 20 Kms/h
            .lazyMap { count++; it.weatherDesc }
            .first()
        assertEquals(5, count)
        assertEquals("Light rain shower", windyDesc)
    }

}