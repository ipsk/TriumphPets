package me.mattstudios.triumphpets.crate.gui

import me.mattstudios.mattcore.MattPlugin
import me.mattstudios.mattcore.utils.MessageUtils.color
import me.mattstudios.mfgui.gui.components.GuiAction
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.components.XMaterial
import me.mattstudios.mfgui.gui.guis.Gui
import me.mattstudios.mfgui.gui.guis.GuiItem
import me.mattstudios.triumphpets.crate.componetents.CrateEffect
import me.mattstudios.triumphpets.crate.componetents.CrateEgg
import me.mattstudios.triumphpets.locale.Message
import me.mattstudios.triumphpets.util.Items
import me.mattstudios.triumphpets.util.Utils.playClickSound
import org.apache.commons.lang.StringUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Matt
 */
class CrateOptionsGui(
        plugin: MattPlugin,
        private val player: Player,
        private var crateEgg: CrateEgg = CrateEgg.BLUE,
        private val crateEffect: CrateEffect = CrateEffect.NONE
) {

    constructor(plugin: MattPlugin, player: Player, crateEffect: CrateEffect) : this(plugin, player, CrateEgg.BLUE, crateEffect)

    private val locale = plugin.locale

    private val gui = Gui(plugin, 5, locale.getMessage(Message.PET_CRATE_GUI_MAIN_TITLE))
    private val eggGui = Gui(plugin, 3, locale.getMessage(Message.PET_CRATE_GUI_EGG_TITLE))
    private val particleGui = Gui(plugin, 5, color("&lSelect particle"))

    init {
        setupGui()
        setupEggGui()
        setupParticleGui()

        gui.open(player)
    }

    /**
     * Adds the main components to the GUI
     */
    private fun setupGui() {
        gui.setDefaultClickAction { it.isCancelled = true }

        gui.filler.fill(GuiItem(Items.FILL_ITEM.getItem()))

        gui.setItem(2, 3, GuiItem(getSelectEggItem(), GuiAction {
            playClickSound(player)
            eggGui.open(player)
        }))

        gui.setItem(2, 7, GuiItem(getSelectParticleItem(), GuiAction {
            playClickSound(player)
            particleGui.open(player)
        }))

        gui.setItem(4, 5, GuiItem(ItemBuilder(XMaterial.EMERALD_BLOCK.parseItem()).build()))

    }

    /**
     * Sets up the egg choose GUI
     */
    private fun setupEggGui() {
        eggGui.setDefaultClickAction { it.isCancelled = true }
        eggGui.setOutsideClickAction { back() }

        eggGui.filler.fill(GuiItem(Items.FILL_ITEM.getItem()))


        // Cycles through all the eggs and sets them
        var slot = 3
        for (egg in CrateEgg.values()) {

            eggGui.setItem(2, slot, GuiItem(
                    ItemBuilder(egg.getItem())
                            .setName(StringUtils.replace(locale.getMessage(Message.PET_CRATE_GUI_EGG_EGG_NAME), "{egg}", egg.eggName))
                            .setLore(color(locale.getMessageRaw(Message.PET_CRATE_GUI_EGG_EGG_LORE)))
                            .build()
            ) {
                crateEgg = egg
                back()
            })

            slot++
        }

        // Back button
        eggGui.setItem(3, 1, GuiItem(
                ItemBuilder(XMaterial.PAPER.parseItem())
                        .setName(locale.getMessage(Message.PET_CRATE_GUI_EGG_BACK_NAME))
                        .setLore(color(locale.getMessageRaw(Message.PET_CRATE_GUI_EGG_BACK_LORE)))
                        .build()
        ) { back() })

    }

    /**
     * Sets up the particles choose GUI
     */
    private fun setupParticleGui() {
        particleGui.setDefaultClickAction { it.isCancelled = true }
        particleGui.setOutsideClickAction { back() }

        particleGui.filler.fill(GuiItem(Items.FILL_ITEM.getItem()))
    }

    /**
     * Back button action
     */
    private fun back() {
        playClickSound(player)
        gui.updateItem(2, 3, getSelectEggItem())
        gui.open(player)
    }

    /**
     * Gets the select egg item for easy updating
     */
    private fun getSelectEggItem(): ItemStack {
        return ItemBuilder(crateEgg.getItem())
                .setName(locale.getMessage(Message.PET_CRATE_GUI_MAIN_EGG_NAME))
                .setLore(color(locale.getMessageRaw(Message.PET_CRATE_GUI_MAIN_EGG_LORE)))
                .build()
    }

    /**
     * Gets the select particle item for easy updating
     */
    private fun getSelectParticleItem(): ItemStack {
        return ItemBuilder(XMaterial.BLAZE_POWDER.parseItem())
                .setName(locale.getMessage(Message.PET_CRATE_GUI_MAIN_PARTICLE_NAME))
                .setLore(color(locale.getMessageRaw(Message.PET_CRATE_GUI_MAIN_PARTICLE_LORE)))
                .build()
    }

}