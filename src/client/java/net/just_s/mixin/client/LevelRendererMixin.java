package net.just_s.mixin.client;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.just_s.PossessiveModClient;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow @Final private LevelTargetBundle targets;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/PostChain;addToFrame(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;IILnet/minecraft/client/renderer/PostChain$TargetBundle;)V",
                    ordinal = 1
            )
    )
    private void possessive$onRenderLevel(PostChain instance, FrameGraphBuilder frameGraphBuilder, int i, int j, PostChain.TargetBundle targetBundle) {
        if (PossessiveModClient.cameraHandler.isEnabled()) {
            PossessiveModClient.cameraHandler.getCamera().onCameraShader(
                    instance,
                    frameGraphBuilder,
                    i, j,
                    this.targets
            );
        } else {
            instance.addToFrame(frameGraphBuilder, i, j, targetBundle);
        }
    }
}
