package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.item.Items;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.SlotActionType;

/** Keeps a Totem of Undying in your offhand, refilling it from your inventory. */
public class AutoTotemModule extends Module {
    private static final int DELAY_TICKS = 3; // pause between swaps; instant swaps get flagged
    private int cooldown = 0;

    public AutoTotemModule() { super("AutoTotem"); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.interactionManager == null) return;
        if (cooldown > 0) { cooldown--; return; }
        if (mc.currentScreen != null) return; // don't fiddle with slots while a GUI is open
        if (mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 36; i++) {
            if (!inv.getStack(i).isOf(Items.TOTEM_OF_UNDYING)) continue;

            // Inventory index -> screen slot id (hotbar 0-8 is slots 36-44, main 9-35 stays)
            int slot = i < 9 ? i + 36 : i;
            // Button 40 = "swap with offhand"
            mc.interactionManager.clickSlot(
                mc.player.playerScreenHandler.syncId, slot, 40, SlotActionType.SWAP, mc.player);
            cooldown = DELAY_TICKS;
            return;
        }
    }
}
