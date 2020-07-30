package blu3.FyreHack.module.modules;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingToggle;

import java.util.Arrays;
import java.util.List;

public class Peek extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Map"), new SettingToggle(true, "Book"), new SettingToggle(true, "Shulker Cmd"));

    public Peek()
    {
        super("Peek", 0, Category.MISC, "Shows content of stuff", settings);
    }
}
