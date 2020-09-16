package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.event.mcevents.EventPlayerClickBlock;
import blu3.ruhamaplus.event.mcevents.EventPlayerDamageBlock;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    public void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> callback)
    {
        EventPlayerClickBlock l_Event = new EventPlayerClickBlock(loc, face);

        RuhamaPlus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
        {
            callback.setReturnValue(false);
            callback.cancel();
        }
    }

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> p_Info)
    {
        EventPlayerDamageBlock l_Event = new EventPlayerDamageBlock(posBlock, directionFacing);
        RuhamaPlus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
        {
            p_Info.setReturnValue(false);
            p_Info.cancel();
        }
    }
}
