package com.ahmadkaddour.sectionlist

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SectionList Sample",
    ) {
        App()
    }
}