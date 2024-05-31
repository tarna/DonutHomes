package dev.tarna.donuthomes.commands

import com.azuyamat.mccollector.builders.ChatCollectorBuilder
import dev.tarna.donuthomes.DonutHomes
import dev.tarna.donuthomes.api.homes.HomeData
import dev.tarna.donuthomes.api.utils.color
import dev.tarna.donuthomes.api.utils.send
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent

class HomesCommand(private val plugin: DonutHomes) : CommandExecutor {
    private val homeManager = plugin.homeManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.send("<red>Only players can use this command!")
            return true
        }

        val gui = getMainHomeGUI(sender)
        sender.openGUI(gui)

        return true
    }

    private fun getMainHomeGUI(player: Player): GUI {
        return gui(plugin, "Homes".color(), GUIType.Chest(6)) {
            fillBorder {
                item = item(Material.GRAY_STAINED_GLASS_PANE) {
                    name = " ".color()
                }
            }

            for (i in 1..14) {
                val home = homeManager.getHome(player, i)
                val xSlotBed = if (i < 8) i + 1 else i - 6
                val ySlotBed = if (i < 8) 2 else 4
                slot(xSlotBed, ySlotBed) {
                    if (home != null) {
                        item = item(Material.LIME_BED) {
                            name = "<gray>Teleport to ${home.name}".color()
                            lore = listOf(
                                "<gray>Click to teleport."
                            ).color()

                            onClick {
                                player.teleport(home.location)
                            }
                        }
                    } else {
                        item = item(Material.LIGHT_GRAY_BED) {
                            name = "<gray>Set home for Home $i".color()
                            lore = listOf(
                                "<gray>Click to set home."
                            ).color()

                            onClick {
                                homeManager.setLocation(player, i, player.location)
                                player.openGUI(getMainHomeGUI(player))
                            }
                        }
                    }
                }

                val xSlotDye = if (i < 8) i + 1 else i - 6
                val ySlotDye = if (i < 8) 3 else 5
                slot(xSlotDye, ySlotDye) {
                    if (home != null) {
                        item = item(Material.LIME_DYE) {
                            name = "<gray>Edit Home ${home.name}".color()
                            lore = listOf(
                                "<gray>Click to open menu."
                            ).color()

                            onClick {
                                player.openGUI(getHomeGUI(player, home))
                            }
                        }
                    } else {
                        item = item(Material.LIGHT_GRAY_DYE) {
                            name = "<green>Edit Home $i".color()
                            lore = listOf(
                                "<red>Please set the home first."
                            ).color()
                        }
                    }
                }
            }
        }
    }

    private fun getHomeGUI(player: Player, home: HomeData): GUI {
        return gui(plugin, "Home Menu".color(), GUIType.Chest(5)) {
            fillBorder {
                item = item(Material.GRAY_STAINED_GLASS_PANE) {
                    name = " ".color()
                }
            }

            slot(3, 3) {
                item = item(Material.NAME_TAG) {
                    name = "<green>Rename Home".color()
                    lore = listOf(
                        "<gray>Click to rename your home."
                    ).color()

                    onClick {
                        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
                        ChatCollectorBuilder {
                            player.sendTitlePart(TitlePart.TITLE, "<green>Rename Home".color())
                            player.sendTitlePart(TitlePart.SUBTITLE, "Enter a new name for your home in chat.".color())
                        }.withTimeout(15000)
                            .onCollect {
                                homeManager.setName(player, home.id, it)
                                Bukkit.getScheduler().runTask(plugin, Runnable {
                                    player.openGUI(getHomeGUI(player, home))
                                })
                            }
                            .onCancel {
                                player.send("<red>Cancelled renaming home.")
                            }
                            .onTimeout {
                                player.send("<red>Timed out renaming home.")
                            }
                            .buildAndRegister(player)
                    }
                }
            }

            slot(5, 3) {
                item = item(Material.COMPASS) {
                    name = "<green>Set Home".color()
                    lore = listOf(
                        "<gray>Click to change your home location."
                    ).color()

                    onClick {
                        homeManager.setLocation(player, home.id, player.location)
                        player.openGUI(getMainHomeGUI(player))
                    }
                }
            }

            slot(7, 3) {
                item = item(Material.RED_DYE) {
                    name = "<red>Delete Home".color()
                    lore = listOf(
                        "<gray>Click to delete your home."
                    ).color()

                    onClick {
                        homeManager.deleteHome(player, home.id)
                        player.openGUI(getMainHomeGUI(player))
                    }
                }
            }
        }
    }
}