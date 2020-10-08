package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.ModuleManager;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Objects;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    private int scrollPos;

    @Shadow @Final private List<ChatLine> drawnChatLines;

    @Shadow public abstract int getLineCount();

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(int left, int top, int right, int bottom, int color) {
        if(!(Objects.requireNonNull(ModuleManager.getModuleByName("BetterChat").isToggled())) || !(Objects.requireNonNull(ModuleManager.getModuleByName("BetterChat"))).getBoolean("NoBackground")) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}