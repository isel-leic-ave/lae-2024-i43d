package pt.isel

import org.junit.jupiter.api.Test

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

}