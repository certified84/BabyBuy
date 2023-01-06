package com.certified.babybuy.di

import com.certified.babybuy.data.repository.Repository
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebase(): Firebase = Firebase

    @Provides
    fun provideRepository(): Repository = Repository()
}