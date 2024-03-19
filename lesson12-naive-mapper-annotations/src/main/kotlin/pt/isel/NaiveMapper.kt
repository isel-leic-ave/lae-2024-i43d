package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.createInstance
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

/**
 * 2nd version - Use a constructor in dest class.
 */
fun <T : Any> Any.mapTo(dest: KClass<T>) : T {
    /**
     * 1st Get all the parameters of a constructor in desst
     */
    val ctor: KFunction<T> = dest
        .constructors
        .first()
    /**
     * 2nd Corresponding Properties in this to ctor.parameters
     */
    val ctorArgs: Map<KParameter, Any?> = this::class
        .memberProperties    // List<KProperty>
        .map { fromProp ->   // List<Pair<KProperty, KParameter?>>
            fromProp to matchParameter(fromProp, ctor.parameters)
        }
        .filter { it.second != null  } // List<Pair<KProperty, KParameter?>>
        .associate { pair ->                   // Map<KParameter, Any?>
            val fromVal = pair.first.call(this)
            val destArg = pair.second!!        // Checked on filter
            destArg to fromVal
        }
    /**
     * 3rd Create an instance of destClass
     */
    return ctor.callBy(ctorArgs)
}

/**
 * 1st simple version copy to Mutable properties
 * and require a parameterless constructor in dest.
 */
fun <T : Any> Any.mapToProps(dest: KClass<T>) : T {
    /**
     * 1st Get all Mutable properties from dest
     */
    val destProps: List<KMutableProperty<*>> = dest
        .memberProperties
        .filter { prop -> prop is KMutableProperty<*> }
        .map { prop -> prop as KMutableProperty<*> }
    /**
     * 2nd Create an instance of dest through default constructor.
     * It fails if dest class does not provide a parameterless constructor
     */
    val target: T = dest.createInstance()
    /**
     * 3rd For each property in the receiver we will look for
     * a corresponding property (i.e. same name and type)
     * in dest and copy the value to the instance created in point 2.
     */
    this::class
        .memberProperties
        .forEach { srcProp ->
            val destProp = matchProperty(srcProp, destProps)
            val fromVal = srcProp.call(this)
            destProp?.setter?.call(target, fromVal)
        }
    return target
}

fun matchProperty(srcProp: KProperty<*>, props: List<KMutableProperty<*>>) : KMutableProperty<*>?{
    return props.firstOrNull { destProp ->
        srcProp.name == destProp.name
        && srcProp.returnType == destProp.returnType
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
