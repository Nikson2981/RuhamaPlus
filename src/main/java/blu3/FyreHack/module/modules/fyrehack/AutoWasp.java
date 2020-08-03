package blu3.FyreHack.module.modules.fyrehack;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingMode;
import blu3.FyreHack.settings.SettingSlider;

import java.util.Arrays;
import java.util.List;

public class AutoWasp extends Module {
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.1D, 4.0D, 1.35D, 2, "Range: "));

    public AutoWasp() { super("AutoWasp", 0, Category.FYREHACK, "1.13 BedAura", settings); }

}
