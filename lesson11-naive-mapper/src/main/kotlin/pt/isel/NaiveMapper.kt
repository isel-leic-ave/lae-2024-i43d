package pt.isel

import kotlin.reflect.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties

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
    val destParameters: List<KParameter> = ctor
        .parameters
    /**
     * 2nd Corresponding properties in this to destParameters
     */
    // val tt: Map<KParameter, Any>
    val ctorArgs: Map<KParameter, Any?> = this::class
        .memberProperties
        .map { fromProp -> fromProp to matchParameter(fromProp, destParameters) }
        .filter { it.second != null  }
        .associate {
            val fromProp = it.first
            val fromVal = fromProp.call(this)
            val destArg = it.second!! // Checked on filter
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
        srcProp.name == arg.name
        && srcProp.returnType == arg.type
    }
}