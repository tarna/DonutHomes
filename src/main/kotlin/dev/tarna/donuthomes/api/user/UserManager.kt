package dev.tarna.donuthomes.api.user

import dev.tarna.donuthomes.DonutHomes
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.TimeUnit

class UserManager(plugin: DonutHomes) : Listener {
    val users = mutableMapOf<UUID, User>()

    init {
        val homesFolder = plugin.dataFolder.resolve("homes")
        if (!homesFolder.exists()) {
            homesFolder.mkdirs()
        }

        plugin.server.pluginManager.registerEvents(this, plugin)
        plugin.server.asyncScheduler.runAtFixedRate(plugin, {
            for (user in users.values) {
                user.save()
            }
        }, 0L, 5L, TimeUnit.MINUTES)
    }

    fun get(uuid: UUID): User {
        return users.getOrPut(uuid) {
            User(uuid)
        }
    }

    fun get(player: Player): User {
        return get(player.uniqueId)
    }

    fun get(player: OfflinePlayer): User {
        return get(player.uniqueId)
    }

    fun destroy(user: User, save: Boolean = true) {
        if (save) user.save()
        users.remove(user.uuid)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = get(player)
        destroy(user)
    }
}