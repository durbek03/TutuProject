package com.example.tutuproject.di

import android.content.Context
import androidx.room.Room
import com.example.tutuproject.others.Constants
import com.example.tutuproject.data.local.AppDatabase
import com.example.tutuproject.data.remote.ApiService
import com.example.tutuproject.domain.CharactersRepImpl
import com.example.tutuproject.domain.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(apiService: ApiService, appDatabase: AppDatabase): CharactersRepository {
        return CharactersRepImpl(apiService, appDatabase)
    }
}