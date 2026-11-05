package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

/** Attacks the closest valid target in range whenever your attack is fully charged. */
public class KillAuraModule extends Module {
    private static final double RANGE = 4.0;
    private static final boolean TARGET_PLAYERS = true;
    private static final boolean TARGET_HOSTILES = true;

    public KillAuraModule() { super("KillAura"); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;
        if (mc.player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        LivingEntity target = findTarget();
        if (target == null) return;

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private LivingEntity findTarget() {
        LivingEntity best = null;
        double bestDist = RANGE * RANGE;
        for (var e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity living) || e == mc.player || !living.isAlive()) continue;

            boolean valid = (TARGET_PLAYERS && e instanceof PlayerEntity p && !p.isSpectator())
                         || (TARGET_HOSTILES && e instanceof HostileEntity);
            if (!valid || !mc.player.canSee(e)) continue;

            double d = mc.player.squaredDistanceTo(e);
            if (d <= bestDist) {
                bestDist = d;
                best = living;
            }
        }
        return best;
    }
}
