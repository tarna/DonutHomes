package dev.tarna.donuthomes.api.homes

import org.bukkit.Location

data class HomeData(
    val id: Int,
    var name: String,
    var location: Location
)