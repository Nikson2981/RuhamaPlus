package blu3.ruhamaplus.mixin.mixins.minecraftforge;


import org.spongepowered.asm.mixin.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.*;
import net.minecraft.client.*;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ EventBus.class })
public class MixinEventBus
{
    @Redirect(method = { "post" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/eventhandler/IEventListener;invoke(Lnet/minecraftforge/fml/common/eventhandler/Event;)V", remap = false), remap = false)
    private void invoke(final IEventListener iEventListener, final Event event) {
        try {
            iEventListener.invoke(event);
        }
        catch (Throwable t3) {
            final String msg = "The event bus encountered an error while invoking event " + event.getClass().getName() + "! Luckily, Ruhama+ is good enougn to prevent this.";
            FMLLog.log.warn(msg);
            try {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(msg));
            }
            catch (Throwable t2) {
                t2.printStackTrace();
            }
            t3.printStackTrace();
        }
    }
}
