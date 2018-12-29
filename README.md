# Kairos Crypto <img src="https://github.com/cjurjiu/KairosCrypto/blob/master/media/icons/kairoscrypto.svg" width="60px" /> [![API](https://img.shields.io/badge/API-21%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=21) [![License](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))
[Kairos](https://en.wikipedia.org/wiki/Kairos) - the right, critical, or opportune moment.

Open Source cryptocurrency viewer for Android, written in Kotlin. MVP architecture. Icon by [Maxim Basinski](https://www.flaticon.com/authors/maxim-basinski) @ [Flat Icon](https://www.flaticon.com).

## Purpose

This project was mainly used as a playground outside work to validate architecture concepts, to learn working with new libraries and to explore various ideas & widgets. Currently the project is no longer under active development, as it achieved its initial purpose.

It is not published to the Google Play store, and there are no plans to publish it anytime soon.

## Tech stack

The project is written fully in Kotlin and is structured in 3 layers: presentation, business & data. Each layer belongs to its own Gradle module.

The interactions between layers respects Bob C. Martin's [dependency rule](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html#the-dependency-rule). Namely, "inner" layers know nothing about any of the outer layers (for instance, the data layer knows nothing about the business layer).

The presentation layer uses the MVP architecture. Presenters are persisted across configuration changes & are re-attached to the new view instance after a configuration change occurs.

Certain things have been left out intentionally (such as proper error handling).

## Data

Retrofit is used to fetch cryptocurrency data from CoinMarketCap's [professional API](https://coinmarketcap.com/api/). Only free to use endpoints are used, but an API key is required. Once you obtain your API key, add it to `local.properties`:

```
coinMarketCapApiKey="<your key here>"
```

A read-to-use apk (for demo purposes) which uses a valid API-key can be found in the latest release.

Cryptocurrency icons are fetched via Glide from [here](https://github.com/cjurjiu/cryptocurrency-icons).

## Tools:

The following tools/tech is used in KairosCrypto:
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