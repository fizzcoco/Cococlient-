package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;

/** Outlines dropped shulker boxes and spawners through walls. */
public class ItemEspModule extends Module {
    private static final double RANGE = 128.0;

    public ItemEspModule() { super("ItemESP"); }

    private static boolean interesting(ItemStack stack) {
        if (stack.isOf(Items.SPAWNER)) return true;
        return stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof ShulkerBoxBlock;
    }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) return;
        Box area = mc.player.getBoundingBox().expand(RANGE);
        for (ItemEntity item : mc.world.getEntitiesByClass(ItemEntity.class, area, e -> true)) {
            item.setGlowing(interesting(item.getStack()));
        }
    }

    @Override
    protected void onDisable() {
        if (mc.world == null || mc.player == null) return;
        Box area = mc.player.getBoundingBox().expand(RANGE);
        for (ItemEntity item : mc.world.getEntitiesByClass(ItemEntity.class, area, e -> true)) {
            item.setGlowing(false);
        }
    }
}
