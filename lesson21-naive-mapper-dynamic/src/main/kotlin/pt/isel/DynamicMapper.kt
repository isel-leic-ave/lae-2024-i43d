package pt.isel

import org.cojen.maker.ClassMaker
import org.cojen.maker.MethodMaker
import org.cojen.maker.Variable
import java.lang.reflect.Constructor
import java.lang.reflect.Parameter
import kotlin.reflect.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaGetter

private val mappers: MutableMap<Pair<KClass<*>, KClass<*>>, Mapper<*>> = mutableMapOf()

fun <T : Any> dynamicMapper(srcKlass: KClass<*>, destKlass: KClass<T>) : Mapper<T> {
    return mappers.getOrPut(srcKlass to destKlass) {
        // 1. Generate an implementation of Mapper to srcKlass and destKlass
        // 2. Create an instance of the class of 1.
        buildMapper(srcKlass, destKlass)
            .finish()
            .kotlin
            .createInstance() as Mapper<*>
    } as Mapper<T>
}

fun <T : Any> buildMapper(srcKlass: KClass<*>, destKlass: KClass<T>) : ClassMaker {
    val cm = ClassMaker
        .begin("Mapper${srcKlass.simpleName}2${destKlass.simpleName}")
        .public_()
        .implement(Mapper::class.java)
    cm
        .addConstructor()
        .public_()
        .invokeSuperConstructor()
    val m = cm
        .addMethod(Any::class.java, "mapFrom", Any::class.java)
        .public_()
    buildMethodMapFrom(m, srcKlass, destKlass)
    return cm
}

fun <T : Any> buildMethodMapFrom(methodMapFrom: MethodMaker, srcKlass: KClass<*>, destKlass: KClass<T>) {
    val init: Constructor<*> = destKlass
        .java
        .constructors
        .first { init -> init.parameters
            .all { param -> srcKlass.memberProperties
                .any { prop ->
                    hasSameName(prop, param)
                    && hasCompatibleType(prop.returnType, param.type)
                }
            }
        }
    val props: List<KProperty<*>> = init.parameters.map { param ->
        srcKlass.memberProperties.first { hasSameName(it, param) }
    }
    val receiver = methodMapFrom.param(0).cast(srcKlass.java)
    val args: Array<Variable> = props.map { prop ->
        receiver.invoke(prop.javaGetter?.name)
    }.toTypedArray()

    val obj = methodMapFrom.new_(destKlass.java, *args)

    methodMapFrom.return_(obj)
}

private fun hasCompatibleType(fromType: KType, destClass: Class<*>): Boolean {
    val fromClass: Class<*> = (fromType.classifier as KClass<*>).java
    if(fromClass.isPrimitive || fromClass == String::class.java) {
        return fromClass == destClass
    } else
        return true
}

private fun hasSameName(srcProp: KProperty<*>, arg: Parameter): Boolean {
    if(srcProp.name == arg.name)
        return true
    val annot = srcProp
        .findAnnotation<MapProp>()
        ?: return false
    return annot.destName == arg.name
}
