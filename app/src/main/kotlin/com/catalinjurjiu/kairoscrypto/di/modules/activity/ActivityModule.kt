package com.catalinjurjiu.kairoscrypto.di.modules.activity

import android.content.Context
import com.catalinjurjiu.kairoscrypto.di.annotations.qualifiers.ActivityContext
import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.ActivityScope
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