package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 3r version - Optimized to make the minimum use of Reflect in function mapFrom()
 * and maximize Reflection in constructor.
 */
class NaiveMapper<T : Any> private constructor (val srcKlass: KClass<*>, val destKlass: KClass<T>) : Mapper<T> {
    companion object {
        private val mappers: MutableMap<Pair<KClass<*>, KClass<*>>, NaiveMapper<*>> = mutableMapOf()

        fun <T : Any> mapper(srcKlass: KClass<*>, destKlass: KClass<T>) :NaiveMapper<T> {
            return mappers.getOrPut(srcKlass to destKlass) {
                NaiveMapper(srcKlass, destKlass)
            } as NaiveMapper<T>
        }
    }

    private var ctor: KFunction<T> = destKlass
        .constructors
        .first()

    private var propsToCtorParameters: List<Pair<KProperty<*>, KParameter?>> = srcKlass
        .memberProperties    // List<KProperty>
        .map { fromProp ->   // List<Pair<KProperty, KParameter?>>
            fromProp to matchParameter(fromProp, ctor.parameters)
        }
        .filter { it.second != null  }

    override fun mapFrom(source: Any): T {
        val ctorArgs = propsToCtorParameters
            .associate { pair ->                   // Map<KParameter, Any?>
                val fromVal = pair.first.call(source)
                val destArg = pair.second!!        // Checked on filter
                destArg to parse(pair.first.returnType, destArg.type, fromVal)
            }
        return ctor.callBy(ctorArgs)
    }
}

private fun parse(fromType: KType, destType: KType, value: Any?): Any? {
    if(value == null) {
        return null
    }
    if ((fromType.classifier as KClass<*>).javaPrimitiveType != null || fromType == typeOf<String>()) {
        return value
    }
    if(fromType.classifier != List::class){
        val mapper = NaiveMapper.mapper(
            fromType.classifier as KClass<*>,
            destType.classifier as KClass<*>
        )
        return mapper.mapFrom(value)
    } else {
        val mapper = NaiveMapper.mapper(
            fromType.arguments[0].type!!.classifier as KClass<*>,
            destType.arguments[0].type!!.classifier as KClass<*>,
        )
        return (value as List<*>).map {
            if(it == null) null
            else mapper.mapFrom(it)
        }

    }

}

fun matchParameter(
    srcProp: KProperty<*>,
    ctorParameters: List<KParameter>) : KParameter?{
    return ctorParameters.firstOrNull { arg ->
        hasCompatibleType(srcProp.returnType, arg.type)
        && hasSameName(srcProp, arg)
    }
}

fun hasCompatibleType(fromType: KType, destType: KType): Boolean {
    if((fromType.classifier as KClass<*>).javaPrimitiveType != null || fromType == typeOf<String>()) {
        return fromType == destType
    } else
        return true
}


fun hasSameName(srcProp: KProperty<*>, arg: KParameter): Boolean {
    if(srcProp.name == arg.name)
        return true
    val annot = srcProp
        .findAnnotation<MapProp>()
        ?: return false
    return annot.destName == arg.name
}
