package net.just_s.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.just_s.PossessiveModClient;
import net.just_s.camera.GameRendererAccess;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererAccess {

    @Unique
    private boolean possessive$hideHand = false;

    @Override
    public void possessive$setRenderHand(boolean bl) {
        possessive$hideHand = !bl;
    }

    // Hides the hand while a camera is active (same hook Freecam uses on 26.2)
    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void possessive$hideHandWhenPossessing(CallbackInfo ci) {
        if (possessive$hideHand) {
            ci.cancel();
        }
    }

    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void possessive$onShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        if (PossessiveModClient.cameraHandler.isEnabled() && !PossessiveModClient.cameraHandler.getCamera().shouldRenderBlockOutline()) {
            cir.setReturnValue(false);
        }
    }
}
