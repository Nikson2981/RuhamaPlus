package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.event.mcevents.EventPlayerDamageBlock;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;

public class PacketMine extends Module {

    public PacketMine() { super("PacketMine", 0, Category.EXPERIMENTAL, "fysgdtvubfh", null); }
    @EventHandler
    private Listener<EventPlayerDamageBlock> OnDamageBlock = new Listener<>(p_Event ->
    {
        if (this.isToggled()) {
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.START_DESTROY_BLOCK, p_Event.getPos(), p_Event.getDirection()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    p_Event.getPos(), p_Event.getDirection()));
            p_Event.cancel();
        }
    });
}
