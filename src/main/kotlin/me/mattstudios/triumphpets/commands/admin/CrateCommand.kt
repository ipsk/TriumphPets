package me.mattstudios.triumphpets.commands.admin

import com.cryptomorin.xseries.XParticle
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.CompleteFor
import me.mattstudios.mf.annotations.Optional
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.annotations.Values
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.triumphpets.TriumphPets
import me.mattstudios.triumphpets.crate.gui.CrateOptionsGui
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator
import org.codemc.worldguardwrapper.WorldGuardWrapper
import org.codemc.worldguardwrapper.flag.WrappedState


/**
 * @author Matt
 */
@Command("pet")
class CrateCommand(private val plugin: TriumphPets) : CommandBase() {

    private val crateManager = plugin.petManager.crateManager
    private val blockStateValues = BlockFace.values().toList().map { it.name }

    /**
     * Crate command that handles both Set and Unset of the Pet crate
     */
    @SubCommand("crate")
    fun crateSet(player: Player, @Values("#crate-type") type: String?, @Optional face: String?) {

        // TODO errors in this fun

        if (type == null) {
            player.sendMessage("Error here")
            return
        }

        val lookingBlock = getLookingBlock(player)

        if (type == "set") {
            val faceExists = !BlockFace.values().toList().map { it.name }.contains(face?.toUpperCase())
            val blockFace = if (face == null || faceExists) BlockFace.WEST_SOUTH_WEST else BlockFace.valueOf(face.toUpperCase())

            setCrate(lookingBlock, player, blockFace)
            return
        }

        unsetCrate(lookingBlock, player)
    }

    /**
     * Completes the values for the crate command, adding values if player is setting and removing if unsetting
     */
    @CompleteFor("crate")
    fun complete(arguments: List<String>): List<String> {
        // Completes with the normal options
        if (arguments.size == 1) {
            return getTabValues(listOf("set", "unset"), arguments[0])
        }

        // Completes with the block face enum
        if (arguments.size == 2 && arguments[0] == "set") {
            return getTabValues(blockStateValues, arguments[1])
        }

        // In case no real value was introduced
        return emptyList()
    }

    /**
     * Sets the crate to the block
     */
    private fun setCrate(block: Block?, player: Player, face: BlockFace) {
        if (block == null) {
            player.sendMessage("Temp message saying error")
            return
        }

        if (crateManager.isCrate(block.location)) {
            player.sendMessage("You already have a crate set!")
            return
        }

        val crateBlock = player.world.getBlockAt(block.location.clone().add(.0, 1.0, .0))

        if (crateBlock.type != Material.AIR) {
            player.sendMessage("Another error")
            return
        }

        val crateOptionsGui = CrateOptionsGui(plugin, player)

        XParticle.cube()


        val wrapper = WorldGuardWrapper.getInstance()
        val flag = wrapper.getFlag("pvp", WrappedState::class.java)

        val state = flag.map { f -> wrapper.queryFlag(player, player.location, f).get() }.orElse(WrappedState.DENY)

        /*setSkullBlock(crateBlock.location, face, Items.CRATE_ITEM.texture)

        crateManager.createCrate(crateBlock.location, face)
        player.sendMessage("Crate created")*/
    }

    /**
     * Removes the crate
     */
    private fun unsetCrate(block: Block?, player: Player) {
        if (block == null) {
            player.sendMessage("Temp message saying error")
            return
        }

        if (!crateManager.isCrate(block.location)) {
            player.sendMessage("Temp message saying error")
            return
        }

        player.sendMessage(block.location.toString())

        crateManager.remove(block.location)
        player.sendMessage("crate removed")
    }

    /**
     * Gets the block the player is looking at
     */
    private fun getLookingBlock(player: Player): Block? {
        val blockIterator = BlockIterator(player, 5)

        var lastBlock = blockIterator.next()

        while (blockIterator.hasNext()) {
            lastBlock = blockIterator.next()

            if (lastBlock.type == Material.AIR) continue

            break
        }

        return if (lastBlock.type == Material.AIR) null else lastBlock
    }

    /**
     * Gets the values to tab complete
     */
    private fun getTabValues(defaultValues: List<String>, argument: String): List<String> {
        val returnValues = mutableListOf<String>()

        // Check if player is typing or not
        if ("" != argument) {

            // Makes the tab completion reduce while typing
            for (value in defaultValues) {
                if (!value.toLowerCase().startsWith(argument.toLowerCase())) continue
                returnValues.add(value)
            }

        } else {
            return defaultValues
        }

        return returnValues
    }

}