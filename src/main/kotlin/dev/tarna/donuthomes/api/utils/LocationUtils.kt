package dev.tarna.donuthomes.api.utils

import org.bukkit.Bukkit
import org.bukkit.Location

fun Location.serializeLocation(rounded: Boolean = false): String {
    return if (rounded) {
        "${world.name},${x.toInt()},${y.toInt()},${z.toInt()},${yaw.toInt()},${pitch.toInt()}"
    } else {
        "${world.name},${x},${y},${z},${yaw},${pitch}"
    }
}

fun String.deserializeLocation(): Location {
    val split = split(",")
    return Location(
        Bukkit.getWorld(split[0]),
        split[1].toDouble(),
        split[2].toDouble(),
        split[3].toDouble(),
        split[4].toFloat(),
        split[5].toFloat()
    )
}