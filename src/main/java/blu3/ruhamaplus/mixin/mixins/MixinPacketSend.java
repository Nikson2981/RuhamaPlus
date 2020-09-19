package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.experimental.FeetXp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin({NetHandlerPlayClient.class})
public class MixinPacketSend
{
    @Inject(
            method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void sendPacket(Packet<?> packetIn, CallbackInfo info) {
        if (ModuleManager.onPacketSend()) {
            info.cancel();
        }

        if (packetIn instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) packetIn;

            if (Minecraft.getMinecraft().player.getHeldItem(packet.getHand()).getItem() instanceof ItemShulkerBox || Minecraft.getMinecraft().player.getHeldItem(packet.getHand()).getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
                BlockPos pos = packet.getPos().offset(packet.getDirection());
                System.out.println("Rightclicked at: " + System.currentTimeMillis());
                RuhamaPlus.friendBlocks.put(pos, 300);
            }
        }
        if (Objects.requireNonNull(ModuleManager.getModuleByName("FootXP").isToggled())){
            try {
                if (!(Minecraft.getMinecraft().player == null) && !(Minecraft.getMinecraft().world == null)) {
                    if (packetIn instanceof CPacketPlayerTryUseItem && Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                        (((FeetXp)Objects.requireNonNull(ModuleManager.getModuleByName("FootXP")))).rotate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}