package dev.tarna.donuthomes.api.homes

import dev.tarna.donuthomes.api.user.UserManager
import dev.tarna.donuthomes.api.utils.send
import dev.tarna.donuthomes.api.utils.serializeLocation
import org.bukkit.Location
import org.bukkit.entity.Player

class HomeManager(private val userManager: UserManager) {
    fun getHomes(player: Player) = userManager.get(player).data.homes

    fun getHome(player: Player, id: Int) = getHomes(player).firstOrNull { it.id == id }

    fun setName(player: Player, id: Int, name: String) {
        val home = getHome(player, id)
        if (home != null) {
            home.name = name
        } else {
            getHomes(player).add(HomeData(id, name, player.location))
        }
        val updatedHome = getHome(player, id) ?: return
        player.send("You have renamed your home to <green>${updatedHome.name}<white>.")
    }

    fun setLocation(player: Player, id: Int, location: Location) {
        val home = getHome(player, id)
        if (home != null) {
            home.location = location
        } else {
            getHomes(player).add(HomeData(id, "Home $id", location))
        }
        val updatedHome = getHome(player, id) ?: return
        player.send("You have set your home named <green>${updatedHome.name} <white>to <green>${updatedHome.location.serializeLocation(true)}.")
    }

    fun deleteHome(player: Player, id: Int) {
        val home = getHomes(player).firstOrNull { it.id == id } ?: return
        getHomes(player).remove(home)
        player.send("You have deleted your home named <green>${home.name}<white>.")
    }
}