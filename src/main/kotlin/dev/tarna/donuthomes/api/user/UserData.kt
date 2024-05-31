package dev.tarna.donuthomes.api.user

import dev.tarna.donuthomes.api.homes.HomeData
import java.util.UUID

data class UserData(
    val uuid: UUID,
    val homes: MutableList<HomeData> = mutableListOf()
)