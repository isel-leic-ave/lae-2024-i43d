package pt.isel

import java.io.PrintStream
import kotlin.reflect.KCallable
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun Appendable.log(obj: Any) {
    this.appendLine("Object of Type ${obj::class.simpleName}")
    obj::class.memberProperties.forEach { prop ->
        prop.isAccessible = true
        appendLine("  - ${prop.name}: ${prop.call(obj)}")
    }
}

fun Appendable.logGetters(obj: Any) {
    this.appendLine("Object of Type ${obj::class.simpleName}")
    obj::class
        .members
        .filter(::isGetter)
        .forEach { func ->
        func.isAccessible = true
        appendLine("  - ${func.name.substring(3)}: ${func.call(obj)}")
    }
}

fun isGetter(m: KCallable<*>): Boolean {
    return m.name.startsWith("get")
            && m.parameters.size == 1
            && m.instanceParameter == m.parameters[0]
            && m.returnType.classifier != Unit::class
}
