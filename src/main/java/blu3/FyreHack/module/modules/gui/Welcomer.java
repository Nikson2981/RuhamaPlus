package blu3.FyreHack.module.modules.gui;

import blu3.FyreHack.gui.AdvancedText;
import blu3.FyreHack.gui.TextWindow;
import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.settings.SettingToggle;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Welcomer extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Text R: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Text G: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Text B: "), new SettingToggle(false, "Shadow"));

    public Welcomer()
    {
        super("Welcomer", 0, Category.GUI, "Welcomes you", settings);

        this.getWindows().add(new TextWindow(50, 12, "Welcomer"));
    }

    public void onOverlay()
    {
        boolean shadow = this.getSettings().get(3).toToggle().state;

        int color = (new Color((int) this.getSettings().get(0).toSlider().getValue(), (int) this.getSettings().get(1).toSlider().getValue(), (int) this.getSettings().get(2).toSlider().getValue())).getRGB();

        this.getWindows().get(0).clearText();
        this.getWindows().get(0).addText(new AdvancedText("Hello " + this.mc.player.getName(), shadow, color));
    }
}
