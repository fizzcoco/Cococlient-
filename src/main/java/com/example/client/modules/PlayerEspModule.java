package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

/** Outlines every other player through walls using the client-side glow flag. */
public class PlayerEspModule extends Module {
    public PlayerEspModule() { super("PlayerESP"); }

    @Override
    public void onTick() {
        if (mc.world == null) return;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;
            p.setGlowing(true);
        }
    }

    @Override
    protected void onDisable() {
        if (mc.world == null) return;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;
            if (!p.hasStatusEffect(StatusEffects.GLOWING)) p.setGlowing(false);
        }
    }
}
