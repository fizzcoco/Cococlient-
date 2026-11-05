package com.example.client.modules;

import com.example.client.Module;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashSet;
import java.util.Set;

/**
 * Finds placed spawners and shulker boxes in loaded chunks and prints each new
 * one to chat with its coordinates and distance. (Chat-based because custom block
 * outline rendering changed a lot in recent versions.)
 */
public class BlockEspModule extends Module {
    private final Set<BlockPos> reported = new HashSet<>();
    private int ticks = 0;

    public BlockEspModule() { super("BlockESP"); }

    @Override
    protected void onEnable() {
        reported.clear();
        ticks = 0;
    }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) return;
        if (ticks++ % 40 != 0) return; // scan every 2 seconds

        int r = mc.options.getClientViewDistance().getValue();
        ChunkPos center = mc.player.getChunkPos();

        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(center.x + dx, center.z + dz);
                if (chunk == null) continue;

                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    String label;
                    if (be instanceof MobSpawnerBlockEntity) label = "Spawner";
                    else if (be instanceof ShulkerBoxBlockEntity) label = "Shulker box";
                    else continue;

                    BlockPos pos = be.getPos().toImmutable();
                    if (!reported.add(pos)) continue;

                    double dist = Math.sqrt(mc.player.squaredDistanceTo(Vec3d.ofCenter(pos)));
                    mc.inGameHud.getChatHud().addMessage(Text.literal(String.format(
                        "[Coconut] %s at %d %d %d (%.0f blocks)",
                        label, pos.getX(), pos.getY(), pos.getZ(), dist)));
                }
            }
        }
    }
}
