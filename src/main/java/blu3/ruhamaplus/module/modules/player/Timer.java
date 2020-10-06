package blu3.ruhamaplus.module.modules.player;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.init.Items;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Timer extends Module {
    public Timer() {
        super("Timer", Keyboard.KEY_NONE, Category.PLAYER, "fsedfsdsadasdrgafdf", settings);
    }

    public static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0, 50, 0.5, 1, "Speed: ")
            );

    public void onDisable(){
        mc.timer.tickLength = 50.0f;
    }

    public void onUpdate() {
        if (this.mc.player != null) {
            mc.timer.tickLength = 50.0f / ((float)this.getSlider("Speed: ") / 10.0f);
        }
    }
}
