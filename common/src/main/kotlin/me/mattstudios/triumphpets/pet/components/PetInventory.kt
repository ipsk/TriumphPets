package me.mattstudios.triumphpets.pet.components

import com.cryptomorin.xseries.XMaterial
import com.cryptomorin.xseries.XSound
import me.mattstudios.mattcore.MattPlugin
import me.mattstudios.mattcore.utils.MessageUtils.color
import me.mattstudios.mattcore.utils.Task.later
import me.mattstudios.mfgui.gui.GUI
import me.mattstudios.mfgui.gui.GuiItem
import me.mattstudios.mfgui.gui.components.GuiAction
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.triumphpets.locale.Message
import me.mattstudios.triumphpets.pet.Pet
import me.mattstudios.triumphpets.pet.utils.Experience
import me.mattstudios.triumphpets.pet.utils.PetType
import me.mattstudios.triumphpets.util.Utils.playClickSound
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack


/**
 * @author Matt
 */
class PetInventory(private val plugin: MattPlugin, private val pet: Pet) {

    private val owner = pet.getOwner()
    private val petMemory = pet.getMemory()

    private val petGui = GUI(plugin, rows(), color(pet.getName() + plugin.locale.getMessage(Message.PET_GUI_TITLE)), true)
    private val filterGui = GUI(plugin, 3, color("&3Filter"))

    /**
     * Starts the GUI related stuff
     */
    init {
        setUpPetGui()
        setUpFilterGUI()

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
            petGui.updateItem(rows() * 9 - 3, ItemBuilder(Material.STONE).setName("Level - " + petMemory.petExperience.level)
                    .setLore("XP - " + petMemory.petExperience.xp).build())
        }, 20L, 20L)
    }

    /**
     * Adds the item to the inventory
     */
    fun addItem(item: Item): Boolean {
        val startAmount = item.itemStack.amount
        val leftOvers = petGui.addItem(item.itemStack)

        if (leftOvers.isEmpty()) {
            petMemory.petExperience.addXp(Experience.getExp(item.itemStack.type, item.itemStack.amount))
            item.remove()
            return true
        }

        val leftItemStack = leftOvers[0] ?: return true
        val amount = startAmount - leftItemStack.amount

        petMemory.petExperience.addXp(Experience.getExp(item.itemStack.type, amount))
        item.setItemStack(leftOvers[0])
        return false
    }

    /**
     * Checks if the inventory is full
     */
    fun isFull(item: ItemStack): Boolean {
        petGui.inventory.storageContents.take(9).forEach() { slot: ItemStack? ->
            if (slot == null) return false
            if (!slot.isSimilar(item)) return@forEach
            if (slot.amount < slot.maxStackSize) return false
        }

        return true
    }

    /**
     * Checks if the player is currently looking at the GUI
     */
    fun isOpened(): Boolean {
        return petGui.inventory.viewers.contains(owner)
    }

    /**
     * Opens the inventory for the player
     */
    fun open() {
        petGui.open(owner)
    }

    /**
     * Sets up the Pet GUI
     */
    private fun setUpPetGui() {
        // Sets the fill items in the bottom
        petGui.setItem(listOf(rows() * 9 - 9, rows() * 9 - 8, rows() * 9 - 6, rows() * 9 - 4, rows() * 9 - 2, rows() * 9 - 1), GuiItem(getFillItem(), GuiAction { it.isCancelled = true }))

        // Sets the filter item for the filter GUI
        petGui.setItem(rows() * 9 - 7, GuiItem(getFilterItem(), GuiAction {
            it.isCancelled = true
            playClickSound(owner)
            filterGui.open(owner)
        }))

        // Sets the close item to close the GUI
        petGui.setItem(rows() * 9 - 5, GuiItem(getCloseItem(), GuiAction {
            it.isCancelled = true

            later(2) {
                playClickSound(owner)
                owner.closeInventory()
            }
        }))

        // Sets the options item to open the options GUI
        petGui.setItem(rows() * 9 - 3, GuiItem(getPetItem(), GuiAction { it.isCancelled = true }))
    }

    /**
     * Sets up the filter GUI
     */
    private fun setUpFilterGUI() {
        // Prevents the drag event
        filterGui.setDragAction { if (it.inventory.type != InventoryType.PLAYER) it.isCancelled = true }
        // Makes it so only cancels clicks in the top inventory
        filterGui.setDefaultTopClickAction { it.isCancelled = true }

        // Fills everything with glass
        filterGui.setFillItem(GuiItem(getFillItem()))

        val air = XMaterial.AIR.parseItem()

        // Opens the slots in the middle for filtering
        filterGui.setItem(10, GuiItem(air))
        filterGui.setItem(11, GuiItem(air))
        filterGui.setItem(12, GuiItem(air))
        filterGui.setItem(13, GuiItem(air))
        filterGui.setItem(14, GuiItem(air))
        filterGui.setItem(15, GuiItem(air))
        filterGui.setItem(16, GuiItem(air))

        // Adds the close item
        filterGui.setItem(22, GuiItem(getCloseItem(), GuiAction {
            playClickSound(owner)
            // Runs 1 tick later to prevent from being stolen from the GUI
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { owner.closeInventory() }, 1L)
        }))

        // Adds the back item
        filterGui.setItem(20, GuiItem(ItemStack(Material.PAPER), GuiAction {
            playClickSound(owner)
            owner.closeInventory()
        }))

        // Adds the toggle white/black list item
        filterGui.setItem(24, GuiItem(getBWItem(), GuiAction {
            playClickSound(owner)
            petMemory.toggleFilterType()
            filterGui.updateItem(24, getBWItem())
        }))

        // Adds the actions for the empty slot clicks
        for (slot in 10..16) addSlotFilterAction(slot)
    }

    /**
     * Gets the rows needed
     */
    private fun rows(): Int {
        return pet.getLevel() + 1
    }

    /**
     * Gets the item to fill the bottom row
     */
    private fun getFillItem(): ItemStack {
        return ItemBuilder(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).setName(" ")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build()
    }

    /**
     * Gets the item for the filter button
     */
    private fun getFilterItem(): ItemStack {
        return ItemBuilder(XMaterial.HOPPER.parseItem()).setName(plugin.locale.getMessage(Message.PET_GUI_FILTER_NAME))
                .setLore(color("&cChange later")).build()
    }

    /**
     * Gets the item for the close button
     */
    private fun getCloseItem(): ItemStack {
        return ItemBuilder(XMaterial.BARRIER.parseItem()).setName(plugin.locale.getMessage(Message.GUI_CLOSE_NAME))
                .setLore(color("&cChange later")).build()
    }

    private fun getBWItem(): ItemStack {
        val filterTypeItem =
                if (petMemory.filterType == FilterType.BLACK_LIST) XMaterial.BLACK_CONCRETE.parseItem()
                else XMaterial.WHITE_CONCRETE.parseItem()
        return ItemBuilder(filterTypeItem).build()
    }

    /**
     * Gets the item for the pet button
     *
     */
    private fun getPetItem(): ItemStack {
        return ItemBuilder(PetType.PET_SNOW_FOX_BABY.item.clone())
                .setName(color("&8• " + pet.getName()))
                .setLore(
                        color("&8• &7Level: &c${pet.getLevel()}&7/&c5"),
                        "",
                        color("&8• &7Picked items: &c5"),
                        color("&8• &7Age: &c5 Days"),
                        color("&8• &7Type: &cSnow Fox"),
                        "",
                        color("&cClick for options"),
                        "",
                        color("&a--------&c-- &88/10xp")
                ).build()
    }

    /**
     * Plays deep xp sound
     */
    private fun xpDeepSound() {
        owner.location.let { XSound.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(owner, .3f, .2f) }
    }

    /**
     * Plays high xp sound
     */
    private fun xpHighSound() {
        owner.location.let { XSound.ENTITY_EXPERIENCE_ORB_PICKUP.playSound(owner, .3f, .8f) }
    }

    /**
     * Adds a slot action to the filters
     */
    private fun addSlotFilterAction(slot: Int) {
        filterGui.addSlotAction(slot, GuiAction {
            val cursor = it.cursor ?: return@GuiAction

            // Removing filter
            if (cursor.type == XMaterial.AIR.parseMaterial() && it.currentItem != null) {
                it.currentItem?.type?.let { type -> petMemory.unFilter(type) }
                filterGui.updateItem(slot, XMaterial.AIR.parseItem())
                xpDeepSound()
                return@GuiAction
            }

            if (cursor.type == XMaterial.AIR.parseMaterial()) return@GuiAction

            // Checks if the Material is already filtered
            if (petMemory.isFiltered(cursor.type)) return@GuiAction

            // Adds the Material to the filter
            val filterItem = ItemBuilder(cursor.type).glow().build()
            petMemory.filter(cursor.type)
            filterGui.updateItem(slot, filterItem)
            xpHighSound()
        })
    }

}