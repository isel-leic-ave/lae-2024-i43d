package pt.isel

import org.junit.jupiter.api.Test
import java.lang.System.out
import kotlin.test.assertEquals

class LoggerTest {

    @Test fun checkLogstudent() {
        val expected = """
            Object of Type Student
              - from: Portugal
              - name: Maria
              - nr: 9873479
        """.trimIndent()
        val st = Student("Maria", 9873479, "Portugal")
        val actual = StringBuilder().also {
            it.log(st)
        }
        assertEquals(expected, actual.toString().trimIndent())
        // out.log(st)
    }
    @Test fun checkLogRectangle() {
        val expected = """
            Object of Type Rectangle
              - area: 20
              - height: 5
              - width: 4
        """.trimIndent()
        val r = Rectangle(4, 5)
        val actual = StringBuilder().also {
            it.log(r)
        }
        assertEquals(expected, actual.toString().trimIndent())
        // out.log(r)
    }
    @Test fun checkLogRectJava() {
        val expected = """
            Object of Type RectJava
              - Width: 4
              - Height: 5
              - Area: 20
           """.trimIndent()
        val r = RectJava(4, 5)
        val actual = StringBuilder().also {
            it.logGetters(r)
        }
        assertEquals(expected, actual.toString().trimIndent())
        // out.log(r)
    }
}