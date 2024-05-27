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
        val writeAndFlush = { nr:Int ->
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .run {
                    write("Meu $nr")
                    write("Super $nr")
                    flush()
                }
        }
        writeAndFlush(1)
        // Eventually Finalization runs and close the File Handle and releases the lock!!
        // Do not do this in production code.
        System.gc()
        writeAndFlush(2)
    }

    @Test
    fun `check overlapping locks with close`() {
        val writeAndFlush = { nr:Int ->
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .run {
                    write("Meu $nr")
                    write("Super $nr")
                    close()
                }
        }
        writeAndFlush(1)
        File(output).readText().also {
            assertEquals("Meu 1Super 1", it)
        }
        writeAndFlush(2)
        File(output).readText().also {
            assertEquals("Meu 2Super 2", it)
        }
    }

    @Test
    fun `check overlapping locks in try with resources`() {
        val writeAndFlush = { nr:Int ->
            FileOutputStream(output)
                .also { it.channel.lock() }
                .bufferedWriter()
                .use {
                    it.write("Meu $nr")
                    it.write("Super $nr")
                }
        }
        writeAndFlush(1)
        File(output).readText().also {
            assertEquals("Meu 1Super 1", it)
        }
        writeAndFlush(2)
        File(output).readText().also {
            assertEquals("Meu 2Super 2", it)
        }
    }

    @Test
    fun `check exclusive writer in use block`() {
        val writeAndFlush = { nr:Int ->
            ExclusiveWriter(output).use {
                it.writer.write("Meu $nr")
                it.writer.write("Super $nr")
            }
        }
        writeAndFlush(1)
        File(output).readText().also {
            assertEquals("Meu 1Super 1", it)
        }
        writeAndFlush(2)
        File(output).readText().also {
            assertEquals("Meu 2Super 2", it)
        }
        System.gc()
    }
    @Test
    fun `check exclusive writer WITHOUT use block`() {
        val writeAndFlush = { nr:Int ->
            ExclusiveWriter(output).also {
                it.writer.write("Meu $nr")
                it.writer.write("Super $nr")
            }
        }
        writeAndFlush(1)
        System.gc()
        Thread.sleep(100)
        File(output).readText().also {
            assertEquals("Meu 1Super 1", it)
        }
        writeAndFlush(2)
        System.gc()
        Thread.sleep(100)
        File(output).readText().also {
            assertEquals("Meu 2Super 2", it)
        }
    }
}

/**
 * Like a BufferedWriter but with a lock
 */
class ExclusiveWriter(path: String) : Closeable {
    val writer = FileOutputStream(path)
        .also { it.channel.lock() }
        .bufferedWriter()

    override fun close() {
        // Nevertheless is does not avoid the Finalization process !!!
        writer.close()
    }
    protected fun finalize() {
        // !!!! May call close for the 2nd time !!!
        writer.close()
    }
}