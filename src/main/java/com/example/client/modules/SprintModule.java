package com.example.client.modules;

import com.example.client.Module;

public class SprintModule extends Module {
    public SprintModule() { super("Sprint"); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.input.hasForwardMovement() && !mc.player.horizontalCollision) {
            mc.player.setSprinting(true);
        }
    }
}
