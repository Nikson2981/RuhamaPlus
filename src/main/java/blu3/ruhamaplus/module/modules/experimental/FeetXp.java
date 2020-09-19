package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

public class FeetXp extends Module {
    public FeetXp() { super ("FootXP", Keyboard.KEY_NONE, Category.EXPERIMENTAL, "make xp go down honhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhonhon", null); }

    public void rotate() {
        if (this.isToggled()) {
            WorldUtils.rotatePacket(mc.player.posX, mc.player.posY - 2, mc.player.posZ);
        }
    }
}