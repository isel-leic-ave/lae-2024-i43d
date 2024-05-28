package pt.isel

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import kotlin.test.assertEquals

private const val output = "ole.txt"

class CloseHandles {
    @AfterEach
    fun cleanup() {
        File(output).delete()
    }

    @Test
    fun `check overlapping locks`() {
        fun writeAndFlush(nr:Int) {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .run {
                    write("My $nr")
                    write("Super $nr")
                    flush()
                }
        }
        writeAndFlush(1)
        // Eventually Finalization runs, closes the File Handle and releases the lock!!
        // Do not do this in real code.
        System.gc()
        writeAndFlush(2)
    }

    @Test
    fun `check overlapping locks with close`() {
        fun writeAndFlush(nr:Int) {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .run {
                    write("My $nr")
                    write("Super $nr")
                    close() // flush() + close() BR => close() OSW => close() StreamEncoder => Release Lock
                }
        }
        writeAndFlush(1)
        File(output).readText().also {
            assertEquals("My 1Super 1", it)
        }
        writeAndFlush(2)
        File(output).readText().also {
            assertEquals("My 2Super 2", it)
        }
    }

    @Test
    fun `check overlapping locks in try with resources`() {
        fun writeAndFlush(nr:Int) {
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .use {
                    it.write("My $nr")
                    it.write("Super $nr")
                }
        }
        writeAndFlush(1)
        File(output).readText().also {
            assertEquals("My 1Super 1", it)
        }
        writeAndFlush(2)
        File(output).readText().also {
            assertEquals("My 2Super 2", it)
        }
    }

    @Test
    fun `check exclusive writer in use block`() {
        fun writeAndFlush(nr:Int) {
            ExclusiveWriter(output).use {
                it.writer.write("My $nr")
                it.writer.write("Super $nr")
            }
        }
        writeAndFlush(1)
        File(output).readText().also {
            assertEquals("My 1Super 1", it)
        }
        writeAndFlush(2)
        File(output).readText().also {
            assertEquals("My 2Super 2", it)
        }
        System.gc() // Run close for 2nd time
    }
    @Test
    fun `check exclusive writer WITHOUT use block`() {
        fun writeAndFlush(nr:Int) {
            ExclusiveWriter(output).also {
                it.writer.write("My $nr")
                it.writer.write("Super $nr")
            }
        }
        writeAndFlush(1)
        System.gc()
        Thread.sleep(100)
        File(output).readText().also {
            assertEquals("My 1Super 1", it)
        }
        writeAndFlush(2)
        System.gc()
        Thread.sleep(100)
        File(output).readText().also {
            assertEquals("My 2Super 2", it)
        }
    }
}

/**
 * Like a BufferedWriter with a lock on channel
 */
class ExclusiveWriter(path: String) : Closeable {
    val writer = FileOutputStream(path)
        .also { it.channel.lock() }
        .bufferedWriter()
    var closeCount = 0
    override fun close() {
        // Nevertheless it does not avoid the Finalization process !!!
        writer.close()
        closeCount++
        println("Run close on ${hashCode()} for $closeCount")
    }
    protected fun finalize() {
        // !!!! May call close for the 2nd time !!!
        this.close()
    }
}