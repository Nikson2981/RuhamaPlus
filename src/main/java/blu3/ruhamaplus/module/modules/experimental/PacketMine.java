package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PacketMine extends Module {

    public PacketMine() { super("PacketMine", 0, Category.EXPERIMENTAL, "fysgdtvubfh", null); }

    public boolean packetMine(BlockPos swerdtfvygbuhni, EnumFacing bruh){
        if (this.isToggled()) {
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.START_DESTROY_BLOCK, swerdtfvygbuhni, bruh));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    swerdtfvygbuhni, bruh));
            return true;
        }
        return false;
    }
}
