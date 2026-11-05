package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/** Client-side night vision effect: no gamma hacks needed. */
public class FullbrightModule extends Module {
    public FullbrightModule() { super("Fullbright"); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.addStatusEffect(
            new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0, false, false));
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
