package blu3.FyreHack.module.modules.render;

import blu3.FyreHack.gui.AdvancedText;
import blu3.FyreHack.gui.TextWindow;
import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.module.ModuleManager;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.settings.SettingToggle;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Gui extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Ruhama R: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Ruhama G: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Ruhama B: "), new SettingToggle(true, "RainbowList"), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "List R: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "List G: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "List B: "));
    public static int arrayListEnd = 160;

    public Gui()
    {
        super("Gui", 0, Category.RENDER, "The Ingame ruhama gui", settings);

        this.getWindows().add(new TextWindow(2, 150, "Arraylist"));
    }

    public void onOverlay()
    {
        this.getWindows().get(0).clearText();

        int color = (new Color((int) this.getSettings().get(0).toSlider().getValue(), (int) this.getSettings().get(1).toSlider().getValue(), (int) this.getSettings().get(2).toSlider().getValue())).getRGB();
        String s = "Fyre Hack 0.2";
        this.getWindows().get(0).addText(new AdvancedText(s, true, color));

        if (this.getSettings().get(3).toToggle().state)
        {
            int age = (int) (System.currentTimeMillis() / 20L % 510L);
            color = (new Color(255, MathHelper.clamp(age > 255 ? 510 - age : age, 0, 255), MathHelper.clamp(255 - (age > 255 ? 510 - age : age), 0, 255))).getRGB();
        } else
        {
            color = (new Color((int) this.getSettings().get(4).toSlider().getValue(), (int) this.getSettings().get(5).toSlider().getValue(), (int) this.getSettings().get(6).toSlider().getValue())).getRGB();
        }

        List<Module> arrayList = ModuleManager.getModules();

        arrayList.remove(this);

        arrayList.sort((a, b) -> Integer.compare(this.mc.fontRenderer.getStringWidth(b.getName()), this.mc.fontRenderer.getStringWidth(a.getName())));

        for (Module m : arrayList)
        {
            if (m.isToggled())
            {
                this.getWindows().get(0).addText(new AdvancedText(m.getName(), true, color));
            }
        }
    }
}
