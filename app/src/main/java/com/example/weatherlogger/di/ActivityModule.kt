package com.example.weatherlogger.di

import com.example.weatherlogger.MainActivity
import com.example.weatherlogger.ui.DetailsFragment
import com.example.weatherlogger.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    internal abstract fun contributeDetailsFragment(): DetailsFragment
}
