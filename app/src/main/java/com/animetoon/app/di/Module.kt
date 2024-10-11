package com.animetoon.app.di

import com.animetoon.app.data.repository.MainRepository
import com.animetoon.app.ui.viewmdel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single {
        MainRepository(androidContext())
    }
    viewModel <MainViewModel> {
        MainViewModel(get())
    }
}