package blu3.ruhamaplus.module.modules.combat;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 **  @author blu3
 **/

public class LongRangeAim extends Module {

    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 150.0D, 150.0D, 2, "Range: "), new SettingMode("Mode: ", "Closest", "Command"));

    public LongRangeAim() {
        super("LongRangeAim", 0, Category.COMBAT, "Automatically aims at either the closest player or whoever you chose", settings);
    }

    public String cmdTarget;

    public void onEnable() {

        if (this.getSettings().get(1).toMode().mode == 1) {
            if (this.cmdTarget == null) {
                ClientChat.warn("No Target Set");
                return;
            } else {
                ClientChat.log("Target: " + this.cmdTarget);
            }
        }
    }

    public void onUpdate() {
        if (this.getSettings().get(1).toMode().mode == 0) {

            //pasted auto32k killaura without the attacking, just finds the closest EntityPlayer

            Entity target = null;

            try {
                List<Entity> players = new ArrayList<>(this.mc.world.playerEntities);
                players.remove(this.mc.player);
                players.sort((a, b) -> Float.compare(a.getDistance(this.mc.player), b.getDistance(this.mc.player)));

                if (players.get(0).getDistance(this.mc.player) < this.getSettings().get(0).toSlider().getValue()) {
                    target = players.get(0);
                }

            } catch (Exception ignored) {}
            if (target == null) {
                return;
            }
            WorldUtils.rotateClient(target.posX, target.posY + 1.0D, target.posZ);
        }

        if (this.getSettings().get(1).toMode().mode == 1) {


            if (this.cmdTarget == null) {
                return;
            }

            // iterates through players in range and sees if any players name matches the command-set target

            Iterator<Entity> entityIterator = this.mc.world.loadedEntityList.iterator();
            while (entityIterator.hasNext()) {
                Entity target = entityIterator.next();
                if (target == mc.player) {
                    continue;
                }
                if (mc.player.getDistance(target) > this.getSettings().get(0).toSlider().getValue()) {
                    continue;
                }
                if (target.getName().equalsIgnoreCase(this.cmdTarget)) {
                    WorldUtils.rotateClient(target.posX, target.posY + 1.0D, target.posZ);
                }
            }
        }
    }
}