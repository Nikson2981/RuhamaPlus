package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import org.lwjgl.input.Keyboard;

public class FeetXp extends Module {
    public FeetXp() { super ("FootXP", Keyboard.KEY_NONE, Category.PLAYER, "make xp go down honhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhon", null); }

  /*  public void rotate() {
        if (this.isToggled()) {
            WorldUtils.rotatePacket(mc.player.posX, mc.player.posY - 2, mc.player.posZ);
        }
    }*/

    public boolean onPacketSend(Packet<?> packet) {
        if (packet instanceof CPacketPlayerTryUseItem){
            WorldUtils.rotatePacket(mc.player.posX, mc.player.posY - 2, mc.player.posZ);
        }
        return false;
    }
}