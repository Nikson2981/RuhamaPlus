package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.player.PacketMine;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> p_Info)
    {
        if ((Objects.requireNonNull(ModuleManager.getModuleByName("PacketMine"))).isToggled()){
            if (((PacketMine) Objects.requireNonNull(ModuleManager.getModuleByName("PacketMine"))).packetMine(posBlock, directionFacing)){
                p_Info.setReturnValue(false);
                p_Info.cancel();
            }
        }
    }
}
