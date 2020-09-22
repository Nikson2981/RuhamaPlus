package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class Criticals extends Module {
    public Criticals() { super("Criticals", 0, Category.CHAT, "HE HACER HE NO JUMP BUT CRITICAL", null); }

    public void performCritical(){
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
    }

}
