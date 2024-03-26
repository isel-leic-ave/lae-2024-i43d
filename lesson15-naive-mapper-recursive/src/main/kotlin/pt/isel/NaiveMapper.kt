package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 3r version - Optimized to make the minimum use of Reflect in function mapFrom()
 * and maximize Reflection in constructor.
 */
class NaiveMapper<T : Any>(val srcKlass: KClass<*>, val destKlass: KClass<T>) {
    private var ctor: KFunction<T> = destKlass
        .constructors
        .first()

    private var propsToCtorParameters: List<Pair<KProperty<*>, KParameter?>> = srcKlass
        .memberProperties    // List<KProperty>
        .map { fromProp ->   // List<Pair<KProperty, KParameter?>>
            fromProp to matchParameter(fromProp, ctor.parameters)
        }
        .filter { it.second != null  }

    fun mapFrom(source: Any): T {
        val ctorArgs = propsToCtorParameters
            .associate { pair ->                   // Map<KParameter, Any?>
                val fromVal = pair.first.call(source)
                val destArg = pair.second!!        // Checked on filter
                destArg to fromVal
            }
        return ctor.callBy(ctorArgs)
    }
}
fun matchParameter(
    srcProp: KProperty<*>,
    ctorParameters: List<KParameter>) : KParameter?{
    return ctorParameters.firstOrNull { arg ->
        srcProp.returnType == arg.type
        && hasSameName(srcProp, arg)
    }
}

fun hasSameName(srcProp: KProperty<*>, arg: KParameter): Boolean {
    if(srcProp.name == arg.name)
        return true
    val annot = srcProp
        .findAnnotation<MapProp>()
        ?: return false
    return annot.destName == arg.name
}
