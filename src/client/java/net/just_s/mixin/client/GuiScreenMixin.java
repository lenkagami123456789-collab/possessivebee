package net.just_s.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.just_s.PossessiveModClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// In 26.2 setScreen moved from Minecraft to Gui.
@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiScreenMixin {
    @ModifyVariable(method = "setScreen", at = @At(value = "HEAD"), argsOnly = true)
    private Screen possessive$onSetScreen(Screen value) {
        if (PossessiveModClient.cameraHandler.isEnabled()) {
            return PossessiveModClient.cameraHandler.getCamera().onSetScreen(value);
        }
        return value;
    }
}
