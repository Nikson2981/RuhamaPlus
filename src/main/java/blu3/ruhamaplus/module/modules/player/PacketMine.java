package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PacketMine extends Module {

    public PacketMine() { super("PacketMine", 0, Category.PLAYER, "fysgdtvubfh", null);
    this.manualTimer = new TimeUtils();
    }



    private BlockPos mining = null;
    private final TimeUtils manualTimer;
    public String m;

    public boolean packetMine(BlockPos swerdtfvygbuhni, EnumFacing bruh){
        if (!(this.mc.world.getBlockState(swerdtfvygbuhni).getBlock() == Blocks.BEDROCK) && !(this.mc.world.getBlockState(swerdtfvygbuhni).getBlock() == Blocks.BARRIER)&& !(this.mc.world.getBlockState(swerdtfvygbuhni).getBlock() == Blocks.END_PORTAL_FRAME)&& !(this.mc.world.getBlockState(swerdtfvygbuhni).getBlock() == Blocks.END_PORTAL)&& !(this.mc.world.getBlockState(swerdtfvygbuhni).getBlock() == Blocks.PORTAL)) {
                this.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (mining == null) this.mining = swerdtfvygbuhni;
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, swerdtfvygbuhni, bruh));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, swerdtfvygbuhni, bruh));
                return true;
        }
        return false;
    }

    public void onUpdate() {
        if (mining == null) {
            return;
        }
        if (this.mc.world.getBlockState(mining).getBlock() == Blocks.AIR){
            mining = null;
        }
        boolean a = false;
        boolean b = false;
        boolean c = false;

        if (!a && manualTimer.passedMs(500)) { //these are not always false, fuck you intellij
            m = "Mining";
            a = true;
        }
        if (!b && manualTimer.passedMs(1000)) {
            m = "Mining.";
            b = true;
        }
        if (!c && manualTimer.passedMs(1500)) {
            m = "Mining..";
            c = true;
        }
        if (manualTimer.passedMs(2000)) {
            m = "Mining...";
            manualTimer.reset();
        }
    }

    public void onRender() {
        if (mining != null) {
            RenderUtils.drawText(mining, m);
        }
    }
}
