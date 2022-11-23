package com.zref.experiment

import org.koin.dsl.module

val appModules = module {
    single { AnuViewModel() }
}