package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MovementInputFromOptions.class, priority = 100000)
public abstract class MixinMovementInputFromOptions extends MovementInput {
    @Redirect(method = "updatePlayerMoveState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        if (ModuleManager.getModuleByName("EnhancedMovement").isToggled()
                && Minecraft.getMinecraft().currentScreen != null
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)
                && Minecraft.getMinecraft().player != null) {
            return Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
        return keyBinding.isKeyDown();
    }
}