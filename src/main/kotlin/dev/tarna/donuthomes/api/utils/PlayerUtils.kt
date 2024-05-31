package dev.tarna.donuthomes.api.utils

import org.bukkit.command.CommandSender

fun CommandSender.send(message: String) = sendMessage(message.color())