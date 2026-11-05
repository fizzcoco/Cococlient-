package com.example.client.modules;

import com.example.client.Module;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * When you charge a respawn anchor with glowstone, places a glowstone block on the
 * anchor's side that faces you, so the explosion is shielded from you.
 *
 * Right-clicking the anchor itself would just charge it again, so the block is placed
 * by clicking a solid neighbour of the target spot instead.
 */
public class AutoAnchorSafeModule extends Module {
    private BlockPos pendingAnchor = null;
    private Hand pendingHand = Hand.MAIN_HAND;
    private int delay = 0;

    public AutoAnchorSafeModule() {
        super("AutoAnchorSafe");

        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (!isEnabled() || !world.isClient() || player != mc.player) return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(Items.GLOWSTONE)
                    && world.getBlockState(hit.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
                pendingAnchor = hit.getBlockPos().toImmutable();
                pendingHand = hand;
                delay = 1; // let the charge go through first
            }
            return ActionResult.PASS;
        });
    }

    @Override
    public void onTick() {
        if (pendingAnchor == null || mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (--delay > 0) return;

        BlockPos anchor = pendingAnchor;
        pendingAnchor = null;

        if (!mc.player.getStackInHand(pendingHand).isOf(Items.GLOWSTONE)) {
            msg("No glowstone left to place.");
            return;
        }

        // Side of the anchor that faces you
        Vec3d toPlayer = mc.player.getEyePos().subtract(Vec3d.ofCenter(anchor));
        Direction dir = Direction.getFacing(toPlayer.x, toPlayer.y, toPlayer.z);
        BlockPos target = anchor.offset(dir);

        if (!mc.world.getBlockState(target).isReplaceable()) return; // already shielded

        // Click a solid block next to the target so the new block lands in the target spot
        for (Direction d : Direction.values()) {
            BlockPos neighbour = target.offset(d);
            if (neighbour.equals(anchor)) continue;

            BlockState st = mc.world.getBlockState(neighbour);
            if (st.isAir() || st.isReplaceable() || st.isOf(Blocks.RESPAWN_ANCHOR)) continue;
            if (!st.isSolidBlock(mc.world, neighbour)) continue;

            Direction face = d.getOpposite();
            Vec3d hitPos = Vec3d.ofCenter(neighbour).add(
                face.getOffsetX() * 0.5, face.getOffsetY() * 0.5, face.getOffsetZ() * 0.5);

            mc.interactionManager.interactBlock(
                mc.player, pendingHand, new BlockHitResult(hitPos, face, neighbour, false));
            mc.player.swingHand(pendingHand);
            return;
        }
        msg("No solid block nearby to place the shield against.");
    }

    private void msg(String text) {
        mc.inGameHud.getChatHud().addMessage(Text.literal("[Coconut] " + text));
    }
}
