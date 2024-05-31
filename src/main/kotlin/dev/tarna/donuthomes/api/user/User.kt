package dev.tarna.donuthomes.api.user

import dev.tarna.donuthomes.DonutHomes
import dev.tarna.donuthomes.api.homes.HomeData
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.UUID

class User(val uuid: UUID) {
    private val plugin = DonutHomes.getInstance()

    private lateinit var file: File
    private lateinit var config: YamlConfiguration

    var data: UserData

    init {
        load()

        data = UserData(
            uuid,
            config.getConfigurationSection("homes")?.getKeys(false)?.map {
                HomeData(
                    it.toInt(),
                    config.getString("homes.$it.name")!!,
                    config.getLocation("homes.$it.location")!!
                )
            }?.toMutableList() ?: mutableListOf()
        )
    }

    private fun load() {
        file = File(plugin.dataFolder.absolutePath + File.separator + "homes" + File.separator + uuid + ".yml")
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        val homes = data.homes
        for (home in homes) {
            config.set("homes.${home.id}.name", home.name)
            config.set("homes.${home.id}.location", home.location)
        }

        config.save(file)
    }
}