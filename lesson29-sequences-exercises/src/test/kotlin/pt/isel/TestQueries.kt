package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class TestQueries {

    @Test fun checkDistinct() {
        val src = sequenceOf( 3, 4, 3, 5, 4, 7, 5, 3)
        val actual = src.lazyDistinct()
        assertContentEquals(
            sequenceOf(3,4,5,7),
            actual
        )
    }

    @Test fun checkDistinctOnNullableElements() {
        val src = sequenceOf( 3, 4, null, 3, 5, null, 4, 7, 5, 3)
        val actual = src.lazyDistinct()
        assertContentEquals(
            sequenceOf(3,4,null, 5,7),
            actual
        )
    }
    @Test fun checkYieldDistinct() {
        val src = sequenceOf( 3, 4, 3, 5, 4, 7, 5, 3)
        val actual = src.yieldDistinct()
        assertContentEquals(
            sequenceOf(3,4,5,7),
            actual
        )
    }

    @Test fun checkYieldDistinctOnNullableElements() {
        val src = sequenceOf( 3, 4, null, 3, 5, null, 4, 7, 5, 3)
        val actual = src.yieldDistinct()
        assertContentEquals(
            sequenceOf(3,4,null, 5,7),
            actual
        )
    }

    @Test fun checkConcat() {
        val actual = sequenceOf(7, 6, 5).concat(sequenceOf(9,8,7))
        assertContentEquals(
            sequenceOf(7,6,5,9,8,7),
            actual
        )
    }
    @Test fun checkCollapse() {
        val actual = sequenceOf(3,3,4,5,3,5,5,4,4,3,2,2,5,2)
        assertContentEquals(
            sequenceOf(3,4,5,3,5,4,3,2,5,2),
            actual
        )
    }
}