package com.catalinj.cryptosmart.di.annotations.scopes

import javax.inject.Scope

/**
 *
 * Identifies a type that the injector only instantiates once per Application. Not inherited.
 *
 * Functionally equivalent to [Singleton][javax.inject.Singleton], but provides better semantics.
 *
 * Created by catalin on 03/05/2018.
 */
@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ApplicationScope