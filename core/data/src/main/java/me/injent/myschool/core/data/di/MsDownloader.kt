package me.injent.myschool.core.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MsDownloader(val type: MsDownloaderType)

enum class MsDownloaderType {
    Android,
    Internal
}