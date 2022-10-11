package com.examplemod.config

import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File


object Config : Vigilant(
    File(ExampleMod.configDirectory, "config.toml"),
    ExampleMod.metadata.name
) {
    var demoSwitch = false
    var demoSelector = 0
    var demoColor : Color = Color.WHITE
    var demoText = ""

    init {
        category("One category") {
            switch(
                ::demoSwitch,
                name = "Switch",
                description = "This is a switch"
            )

            subcategory("An additional category") {
                selector(
                    ::demoSelector,
                    name = "Selector",
                    description = "This is a selector",
                    options = listOf("Option 1", "Option 2", "Option 3")
                )
                color(
                    ::demoColor,
                    name = "Color",
                    description = "This sets a color"
                )
            }
        }

        category("Another category") {
            text(
                ::demoText,
                name = "Text",
                description = "This is text",
                placeholder = "This is some placeholder text."
            )

        }
    }


}