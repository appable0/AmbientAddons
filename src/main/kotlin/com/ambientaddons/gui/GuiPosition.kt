package com.ambientaddons.gui

import kotlinx.serialization.Serializable

@Serializable
data class GuiPosition (
    var x: Double,
    var y: Double,
    var scale: Double
)