package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.event.events.EventSendPacket;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.WorldUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

public class FeetXp extends Module {
    public FeetXp() { super ("FootXP", Keyboard.KEY_NONE, Category.MISC, "Automatically points xp at feet", null); }

    @Subscribe
    public void sendPacket(EventSendPacket event) {
        if (mc.world == null || mc.player == null) return;
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            event.setCancelled(true);
            WorldUtils.rotatePacket(mc.player.posX, mc.player.posY - 2, mc.player.posZ);
            this.mc.playerController.processRightClick(this.mc.player, this.mc.world, EnumHand.MAIN_HAND);
        }
    }
}