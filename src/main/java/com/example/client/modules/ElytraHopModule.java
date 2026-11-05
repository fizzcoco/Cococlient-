package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;

/**
 * With an elytra equipped: jumps automatically, then taps jump in mid-air while
 * falling so the elytra deploys on its own. Land and it hops straight back up.
 */
public class ElytraHopModule extends Module {
    private int ticks = 0;

    public ElytraHopModule() { super("ElytraHop"); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) return;
        if (mc.player.isTouchingWater() || mc.player.hasVehicle()) return;

        if (mc.player.isGliding()) {
            mc.options.jumpKey.setPressed(false);
            return;
        }

        if (mc.player.isOnGround()) {
            mc.options.jumpKey.setPressed(true);          // hop
        } else if (mc.player.getVelocity().y < 0) {
            // Fresh key press while falling deploys the elytra, so alternate each tick.
            mc.options.jumpKey.setPressed((ticks++ & 1) == 0);
        }
    }

    @Override
    protected void onDisable() {
        mc.options.jumpKey.setPressed(false);
        ticks = 0;
    }
}
