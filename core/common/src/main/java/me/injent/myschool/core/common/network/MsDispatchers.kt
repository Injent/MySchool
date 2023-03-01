package me.injent.myschool.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val msDispatcher: MsDispatchers)

enum class MsDispatchers {
    IO
}