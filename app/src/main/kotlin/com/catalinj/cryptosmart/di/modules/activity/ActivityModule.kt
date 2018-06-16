package com.catalinj.cryptosmart.di.modules.activity

import android.content.Context
import com.catalinj.cryptosmart.di.annotations.qualifiers.ActivityContext
import com.catalinj.cryptosmart.di.annotations.scopes.ActivityScope
import com.example.cryptodrawablesprovider.GithubCryptoIconHelper
import com.example.cryptodrawablesprovider.ImageHelper
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activityContext: Context) {

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideActivityContext(): Context {
        return activityContext
    }

    @Provides
    @ActivityScope
    fun provideImageHelper(@ActivityContext context: Context): ImageHelper<String> {
        return GithubCryptoIconHelper(context)
    }
}