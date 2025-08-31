# SectionList
[![Maven Central](https://img.shields.io/maven-central/v/io.github.ahmad-kaddour/section-list.svg)](https://central.sonatype.com/artifact/io.github.ahmad-kaddour/section-list)
[![License](https://img.shields.io/github/license/ahmad-kaddour/sectionlist.svg)](https://github.com/ahmad-kaddour/sectionlist/blob/main/LICENSE)
![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blueviolet?logo=kotlin)

A Kotlin Multiplatform library for categorizing and grouping lazy list items into sections, with the ability to scroll to a specific section and track the currently visible section. This allows you to synchronize it with other UI elements such as a `TabRow`.

This library is optimized, it uses a custom algorithm to track the current section instead of treating the entire section as a single element, which can cause performance issues.

## Platform support

`SectionList` supports these platforms:

1. [x] Android (SDK > 24)
2. [x] iOS
3. [x] Desktop (JVM)
4. [x] JS/Wasm

## Installation

`SectionList` is available on **Maven Central**. Add the following dependency to your project:

```kotlin
implementation("io.github.ahmad-kaddour:section-list:1.0.0")
```

## License

Copyright (C) 2023 Ahmad Haj Kaddour

Licensed under the Apache License, Version 2.0
