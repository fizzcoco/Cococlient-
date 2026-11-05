package com.example.client.mixin;

import com.example.client.modules.XRayModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    /**
     * If this fails to apply on 1.21.11, the game log will print the real
     * descriptor of Block.shouldDrawSide. Match the parameters to that.
     */
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void coconut$xray(BlockState state, BlockView world, BlockPos pos,
                                     Direction side, BlockPos otherPos,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (XRayModule.active) {
            cir.setReturnValue(XRayModule.isVisible(state));
        }
    }
}
