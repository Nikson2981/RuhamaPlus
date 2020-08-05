package blu3.FyreHack.module.modules.fyrehack;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.utils.WorldUtils;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoWasp extends Module {
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.1D, 4.0D, 1.35D, 2, "Range: "));

    public AutoWasp() {
        super("WaspAim", 0, Category.FYREHACK, "Automatically aims at either the closest player or whoever you chose", settings);
    }

    public void onUpdate() {


        Entity target = null;

        try {
            List<Entity> players = new ArrayList<>(this.mc.world.playerEntities);
            players.remove(this.mc.player);

            players.sort((a, b) -> Float.compare(a.getDistance(this.mc.player), b.getDistance(this.mc.player)));

            if (players.get(0).getDistance(this.mc.player) < this.getSettings().get(0).toSlider().getValue()) {
                target = players.get(0);
            }
        } catch (Exception ignored) {
        }

        if (target == null) {
            return;
        }

        WorldUtils.rotateClient(target.posX, target.posY + 1.0D, target.posZ);
    }
}


