package pt.isel.annotations

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import pt.isel.Artist
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.test.assertTrue

class PlayWithAnnotations {
    @Test fun checkAllAnnotations() {
        Account::class
            .annotations
            .forEach { annot: Annotation ->
                println(annot)
            }
    }
    @Test fun checkForAnnotationDummy() {
//        val accountHasDummy = Account::class
//            .annotations
//            .filterIsInstance<Dummy>()
//            .isNotEmpty()

        val accountHasDummy = Account::class
            .findAnnotation<Dummy>() != null
        assertTrue(accountHasDummy)
        val artistHasDummy = Artist::class
            .annotations
            .filter { it is Dummy }
            .isNotEmpty()
        assertFalse(artistHasDummy)
    }
    @Test fun checkAnnotationOnPropertiesOfAccount() {
        Account::class
            .memberProperties
            .forEach {
                val hasDummy = it.hasAnnotation<Dummy>()
                println("property ${it.name} is dummy = $hasDummy")
            }

    }
    @Test fun checkAnnotationOnCtorParameters() {
        Account::class
            .constructors
            .first()
            .parameters
            .forEach {
                val hasDummy = it.hasAnnotation<Dummy>()
                println("parameter ${it.name} is dummy = $hasDummy")
            }

    }

}