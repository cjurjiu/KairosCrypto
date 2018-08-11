package com.catalinjurjiu.kairoscrypto.di.annotations.scopes

import javax.inject.Scope

/**
 * Annotation which marks components scoped to CoinList use case.
 *
 * Created by catalinj on 04.02.2018.
 */
@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class CoinListScope