package com.example.client.modules;

import com.example.client.Module;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * Hides every block except ores and a few valuables. The actual hiding happens in
 * BlockMixin (Block.shouldDrawSide); this class holds the on/off flag and the filter.
 * Turn on Fullbright too, since caves are dark.
 */
public class XRayModule extends Module {
    public static volatile boolean active = false;

    public XRayModule() { super("XRay"); }

    public static boolean isVisible(BlockState state) {
        return state.isIn(ConventionalBlockTags.ORES)
            || state.isOf(Blocks.ANCIENT_DEBRIS)
            || state.isOf(Blocks.SPAWNER)
            || state.isOf(Blocks.CHEST)
            || state.isOf(Blocks.ENDER_CHEST)
            || state.isOf(Blocks.LAVA)
            || state.isOf(Blocks.DIAMOND_BLOCK)
            || state.isOf(Blocks.GOLD_BLOCK);
    }

    @Override
    protected void onEnable() {
        active = true;
        reloadWorld();
    }

    @Override
    protected void onDisable() {
        active = false;
        reloadWorld();
    }

    private void reloadWorld() {
        if (mc.worldRenderer != null) mc.worldRenderer.reload(); // rebuild chunk meshes
    }
}
