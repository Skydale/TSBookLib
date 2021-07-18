package io.github.mg138.tsbook

import dev.reactant.reactant.core.ReactantPlugin
import io.github.mg138.tsbook.config.BookConfig
import org.bukkit.GameRule
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

// TODO replace putIfAbsent with computeIfAbsent
//      also don't question why i put this here

@ReactantPlugin([
    "io.github.mg138.tsbook"
])
class Book : JavaPlugin() {
    companion object {
        lateinit var inst: Book
        lateinit var jar: File
    }

    override fun onEnable() {
        inst = this
        jar = inst.file
        preReg()
        load()
        reg()
    }

    override fun onDisable() {
        unload()
    }

    fun unload() {
        BookConfig.unload()
    }

    fun load() {
        BookConfig.load(inst, jar)
    }

    private fun preReg() {
    }

    private fun reg() {
        server.worlds.forEach { it.setGameRule(GameRule.KEEP_INVENTORY, true) }

        getCommand("armor")!!.setExecutor(Armor)
    }
}