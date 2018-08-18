# Kairos Crypto <img src="https://github.com/cjurjiu/KairosCrypto/blob/master/media/icons/kairoscrypto.svg" width="60px" /> [![API](https://img.shields.io/badge/API-21%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=21) [![License](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))
[Kairos](https://en.wikipedia.org/wiki/Kairos) - the right, critical, or opportune moment.

Open Source cryptocurrency viewer for Android, written in Kotlin. MVP architecture. Icon by [Maxim Basinski](https://www.flaticon.com/authors/maxim-basinski) @ [Flat Icon](https://www.flaticon.com).

## Purpose

This project is mainly used as a playground to validate architecture concepts, to learn working with new libraries & frameworks and to play with various ideas & widgets.

Currently the project is not published to the Google Play store, though it will certainly be published at some point.

New features will be added as time progresses.

## Tech stack

The project is written fully in Kotlin and is structured in 3 layers: presentation, business & data. Eacy layer belongs to its own Gradle module.

The interactions between layers respects Bob C. Martin's [dependency rule](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html#the-dependency-rule). Namely, "inner" layers know nothing about any of the outer layers (for instance, the data layer knows nothing about the business layer).

The presentation layer uses the MVP architecture. Presenters are persisted across configuration changes & are re-attached to the new view instance after a configuration change occurs.

Data is fetched from CoinMarketCap's [public API](https://coinmarketcap.com/api/) using Retrofit.

Tools used:
- [Kotlin](https://kotlinlang.org/)
- [RxJava2](https://github.com/ReactiveX/RxJava) (with RxJava's Android extensions)
- [RxRelay](https://github.com/JakeWharton/RxRelay)
- [Dagger2](https://github.com/google/dagger) (without the Android extensions)
- [Wheelbarrow](https://github.com/cjurjiu/Wheelbarrow) (to store Dagger2 components/Presenters across config changes)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Retrofit](http://square.github.io/retrofit/)
- [Gson](https://github.com/google/gson)
- [Glide](https://github.com/bumptech/glide)
- [Material Progress](https://github.com/DreaminginCodeZH/MaterialProgressBar)
- [Android SVG](http://bigbadaboom.github.io/androidsvg/)
- [Leak Canary](https://github.com/square/leakcanary)
- [Jsoup](https://jsoup.org/)

## Contributing

Feel free to contribute with ideas, bug reports or even features.