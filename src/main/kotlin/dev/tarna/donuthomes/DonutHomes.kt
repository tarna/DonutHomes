package dev.tarna.donuthomes

import com.azuyamat.mccollector.CollectorRegistry
import dev.tarna.donuthomes.api.homes.HomeManager
import dev.tarna.donuthomes.api.user.UserManager
import dev.tarna.donuthomes.commands.HomesCommand
import org.bukkit.plugin.java.JavaPlugin

class DonutHomes : JavaPlugin() {
    companion object {
        fun getInstance() = getPlugin(DonutHomes::class.java)
    }

    lateinit var userManager: UserManager
    lateinit var homeManager: HomeManager

    override fun onEnable() {
        val now = System.currentTimeMillis()

        userManager = UserManager(this)
        homeManager = HomeManager(userManager)

        CollectorRegistry.init(this)

        getCommand("homes")?.setExecutor(HomesCommand(this))

        logger.info("DonutHomes enabled in ${System.currentTimeMillis() - now}ms")
    }

    override fun onDisable() {
        val now = System.currentTimeMillis()

        for (user in userManager.users.values) {
            userManager.destroy(user)
        }

        logger.info("DonutHomes disabled in ${System.currentTimeMillis() - now}ms")
    }
}