package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class Criticals extends Module {
    public Criticals() { super("Criticals", 0, Category.COMBAT, "HE HACER HE NO JUMP BUT CRITICAL", null); }

    public void performCritical(){ // pre-alpine autism
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
    }
/*    int i = 0;
    @Override
    public void fastUpdate() {
       mc.player.cameraPitch = i;
        i++;
        if (i == 180) i = 0;
    }

    @Override
    public void onUpdate() {

    }*/
}
