package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.utils.ClientChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.Explosion;

import java.util.List;


public class EnhancedMovement extends Module {
    public EnhancedMovement() {
        super("EnhancedMovement", 0, Category.MISC, "HE HACER NO KB AND MOVE IN INVENTORY REEEEEEE", null);
    }

    public boolean onPacketRead(Packet<?> packet) {
        if (packet instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity knockback = (SPacketEntityVelocity) packet;
            if (knockback.getEntityID() == this.mc.player.getEntityId()) {
                return true;
            }
        } else if (packet instanceof SPacketExplosion) {
            SPacketExplosion explosion = (SPacketExplosion) packet;
            return true;
        }
        return false;
    }


    public void onUpdate() {
        if (mc.gameSettings.keyBindSprint.isPressed()) mc.player.setSprinting(true);
    }

    public boolean collision(Entity e){
        if (this.isToggled() && e == this.mc.player) return true;
        else return false;
    }
}
