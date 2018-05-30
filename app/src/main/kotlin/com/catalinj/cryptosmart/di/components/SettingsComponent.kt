package com.catalinj.cryptosmart.di.components

import com.catalinj.cryptosmart.di.annotations.scopes.SettingsScope
import com.catalinj.cryptosmart.di.modules.settings.SettingsModule
import com.catalinj.cryptosmart.presentationlayer.features.settings.view.SettingsFragment
import dagger.Subcomponent

/**
 * Created by catalin on 06/05/2018.
 */
@SettingsScope
@Subcomponent(modules = [SettingsModule::class])
abstract class SettingsComponent {

    abstract fun inject(settingsFragment: SettingsFragment)
}