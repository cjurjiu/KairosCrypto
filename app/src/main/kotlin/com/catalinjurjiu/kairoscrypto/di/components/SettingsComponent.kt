package com.catalinjurjiu.kairoscrypto.di.components

import com.catalinjurjiu.kairoscrypto.di.annotations.scopes.SettingsScope
import com.catalinjurjiu.kairoscrypto.di.modules.settings.SettingsModule
import com.catalinjurjiu.kairoscrypto.presentationlayer.features.settings.view.SettingsFragment
import dagger.Subcomponent

/**
 * Created by catalin on 06/05/2018.
 */
@SettingsScope
@Subcomponent(modules = [SettingsModule::class])
abstract class SettingsComponent {

    abstract fun inject(settingsFragment: SettingsFragment)
}