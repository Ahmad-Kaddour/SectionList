# SectionList
[![Maven Central](https://img.shields.io/maven-central/v/io.github.ahmad-kaddour/section-list.svg)](https://central.sonatype.com/artifact/io.github.ahmad-kaddour/section-list)
[![License](https://img.shields.io/github/license/ahmad-kaddour/sectionlist.svg)](https://github.com/ahmad-kaddour/sectionlist/blob/main/LICENSE)
![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blueviolet?logo=kotlin)

A Kotlin Multiplatform library for categorizing and grouping lazy list items into sections, with the ability to scroll to a specific section and track the currently visible section. This allows you to synchronize it with other UI elements such as a `TabRow`.

This library is optimized, it uses a custom algorithm to track the current section instead of treating the entire section as a single element, which can cause performance issues.

## Demo

| Android | iOS |
|---------|-----|
| ![Android Demo](https://media.giphy.com/media/cMV5ab4aCTXnQwdppq/giphy.gif) | ![iOS Demo](https://media.giphy.com/media/Miov3Mvn9tKnMpPcG9/giphy.gif) |

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

## Usage

### 1. Create a SectionListState
Pass a list of section sizes (including headers/footers).  
Use `rememberSectionListState` to preserve state across configuration changes.

```kotlin
val sectionListState = rememberSectionListState(
    sectionsSize = sections.map { it.items.size + 1 } // +1 for header
)
```

You can also provide an existing `LazyListState`:

```kotlin
val sectionListState = rememberSectionListState(
    sectionsSize = sections.map { it.items.size + 1 },
    lazyListState = LazyListState(firstVisibleItemIndex = 1)
)
```

---

### 2. Link to Your Lazy List
Attach the `sectionListState` to a `LazyColumn`:

```kotlin
LazyColumn(
    state = sectionListState.lazyListState,
    modifier = Modifier.nestedScroll(sectionListState.nestedScrollConnection)
) {
    // items...
}
```

---

### 3. Observe the Current Section
You can track the visible section index:

```kotlin
val currentSection = sectionListState.currentSectionIndex.collectAsStateWithLifecycle(initialValue = 0)
```

---

### 4. Scroll to a Section
Programmatically scroll to a specific section using coroutines:

```kotlin
val coroutineScope = rememberCoroutineScope()
coroutineScope.launch {
    sectionListState.animateScrollToSection(sectionIndex = 1)
}
```

---

### 5. Populate the Lazy List

#### Manual Population
```kotlin
LazyColumn(
    state = sectionListState.lazyListState,
    modifier = Modifier.nestedScroll(sectionListState.nestedScrollConnection)
) {
    for (section in sections) {
        item {
            SectionHeader(title = section.title)
        }
        for (item in section.items) {
            item {
                ItemView(model = item)
            }
        }
    }
}
```

#### Using DSL Functions
Instead of manual loops, you can use one of the provided DSL functions:  
`section`, `sectionIndexed`, `sections`, or `sectionsIndexed`.

```kotlin
LazyColumn(
    state = sectionListState.lazyListState,
    modifier = Modifier
        .nestedScroll(sectionListState.nestedScrollConnection)
) {
    sections(
        sections = sections,
        sectionItems = { section -> section.items },
        headerContent = { section -> SectionHeader(title = section.title) },
        itemContent = { item -> ItemView(model = item) }
    )
}
```

---

### 6. Full Example
Check out the [sample app](https://github.com/Ahmad-Kaddour/SectionList/tree/main/sample) for a complete implementation.


## License

Copyright (C) 2025 Ahmad Haj Kaddour

Licensed under the Apache License, Version 2.0
