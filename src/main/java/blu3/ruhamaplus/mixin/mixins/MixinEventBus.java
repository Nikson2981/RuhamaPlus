package blu3.ruhamaplus.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.*;
import net.minecraft.client.*;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EventBus.class)
public class MixinEventBus
{
    @Redirect(method = { "post" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/eventhandler/IEventListener;invoke(Lnet/minecraftforge/fml/common/eventhandler/Event;)V", remap = false), remap = false)
    private void invoke(final IEventListener v_1, final Event v0) {
        try {
            v_1.invoke(v0);
        }
        catch (Throwable v) {
            final String a2 = "An error occured at  " + v0.getClass().getName() + ". Luckily, Ruhama+ is good enough to prevent this.";
            FMLLog.log.warn(a2);
            try {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(a2));
            }
            catch (Throwable a3) {
                a3.printStackTrace();
            }
            v.printStackTrace();
        }
    }
}
