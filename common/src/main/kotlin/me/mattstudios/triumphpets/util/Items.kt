package me.mattstudios.triumphpets.util

import com.cryptomorin.xseries.XMaterial
import me.mattstudios.mfgui.gui.components.ItemBuilder
import org.bukkit.inventory.ItemStack

/**
 * @author Matt
 */
enum class Items(val item: ItemStack, val texture: String) {

    // Empty item for the pets menu
    EMPTY_PET_ITEM(
            ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZWExZjUxZjI1M2ZmNTE0MmNhMTFhZTQ1MTkzYTRhZDhjM2FiNWU5YzZlZWM4YmE3YTRmY2I3YmFjNDAifX19")
                    .build(),
            ""
    ),

    // Cate item for the pets crate
    CRATE_ITEM(
            ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                    .setSkullTexture("eyJ0aW1lc3RhbXAiOjE1ODU5NjUwOTQwMzksInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xOTgyMmE3YWY4YWU1MGVlOGM1NzQzOGM4ZjgwODFjNGI1ZmUwY2Y5OGQzNDdiNjc4ZjExZWIxODhiMmUwMDdjIn19fQ==")
                    .build(),
            "eyJ0aW1lc3RhbXAiOjE1ODU5NjUwOTQwMzksInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xOTgyMmE3YWY4YWU1MGVlOGM1NzQzOGM4ZjgwODFjNGI1ZmUwY2Y5OGQzNDdiNjc4ZjExZWIxODhiMmUwMDdjIn19fQ=="
    )

}