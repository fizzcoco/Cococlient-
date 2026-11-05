package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;

/**
 * Auto-attacks the closest player while you hold a mace AND wear a chestplate.
 * Wearing an elytra in the chest slot (your elytra-mace setup) disables it,
 * so it never swings while you're set up for gliding.
 */
public class AutoMaceModule extends Module {
    private static final double RANGE = 3.0;      // vanilla survival reach
    private static final float MIN_COOLDOWN = 1.0f; // wait for full charge

    public AutoMaceModule() { super("AutoMace"); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;

        // Must be holding a mace
        if (!mc.player.getMainHandStack().isOf(Items.MACE)) return;

        // Must be wearing an actual chestplate (elytra / empty slot -> do nothing)
        ItemStack chest = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        if (chest.isEmpty() || chest.isOf(Items.ELYTRA) || !chest.isIn(ItemTags.CHEST_ARMOR)) return;

        if (mc.player.getAttackCooldownProgress(0.5f) < MIN_COOLDOWN) return;

        PlayerEntity target = findTarget();
        if (target == null) return;

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private PlayerEntity findTarget() {
        PlayerEntity best = null;
        double bestDist = RANGE * RANGE;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player || !p.isAlive() || p.isSpectator()) continue;
            if (!mc.player.canSee(p)) continue;
            double d = mc.player.squaredDistanceTo(p);
            if (d <= bestDist) {
                bestDist = d;
                best = p;
            }
        }
        return best;
    }
}
