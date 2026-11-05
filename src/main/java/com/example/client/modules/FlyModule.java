package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.util.math.Vec3d;

/**
 * Velocity-based flight: WASD moves you, Space goes up, Shift goes down, and you
 * hover in place when idle. Works in singleplayer survival. Most multiplayer
 * servers have anti-fly and will kick or rubber-band you.
 */
public class FlyModule extends Module {
    private static final double SPEED = 0.6;

    public FlyModule() { super("Fly"); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.player.hasVehicle()) return;

        double yaw = Math.toRadians(mc.player.getYaw());
        double forward = (mc.options.forwardKey.isPressed() ? 1 : 0) - (mc.options.backKey.isPressed() ? 1 : 0);
        double strafe  = (mc.options.leftKey.isPressed() ? 1 : 0) - (mc.options.rightKey.isPressed() ? 1 : 0);

        double vx = (-Math.sin(yaw) * forward + Math.cos(yaw) * strafe) * SPEED;
        double vz = ( Math.cos(yaw) * forward + Math.sin(yaw) * strafe) * SPEED;
        double vy = (mc.options.jumpKey.isPressed() ? SPEED : 0) - (mc.options.sneakKey.isPressed() ? SPEED : 0);

        mc.player.setVelocity(new Vec3d(vx, vy, vz));
        mc.player.fallDistance = 0;
    }
}
