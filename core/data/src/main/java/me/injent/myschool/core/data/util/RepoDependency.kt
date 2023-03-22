package me.injent.myschool.core.data.util

import kotlin.reflect.KClass

/**
 * Applying [RepoDependency] to a repository means that annotated repository must
 * be used only after completed synchronization of class provided in annotation
 */
@Target(AnnotationTarget.CLASS)
annotation class RepoDependency(vararg val depends: KClass<*>)