package me.injent.myschool.core.data.util

import me.injent.myschool.core.data.repository.Syncable
import kotlin.reflect.KClass

/**
 * Applying [RepoDependency] to a [Syncable] repository means that annotated repository needs
 * to be synchronized after completed synchronization of class provided in annotation
 *
 * @param depends a repository which implements [Syncable] that needs to be synchronized before
 * annotated repository synchronization
 */
@Target(AnnotationTarget.CLASS)
annotation class RepoDependency(vararg val depends: KClass<out Syncable>)