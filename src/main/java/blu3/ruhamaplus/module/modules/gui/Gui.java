package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.gui.AdvancedText;
import blu3.ruhamaplus.gui.TextWindow;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Gui extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Ruhama R: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Ruhama G: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "Ruhama B: "), new SettingToggle(true, "RainbowList"), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "List R: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "List G: "), new SettingSlider(0.0D, 255.0D, 235.0D, 0, "List B: "), new SettingToggle(true, "ClientName"));
    public static int arrayListEnd = 160;

    public Gui()
    {
        super("ArrayList", 0, Category.GUI, "Pretty much... zero.", settings);

        this.getWindows().add(new TextWindow(2, 150, "Arraylist"));
    }

    public void onOverlay()
    {
        this.getWindows().get(0).clearText();

        int color = (new Color((int) this.getSettings().get(0).asSlider().getValue(), (int) this.getSettings().get(1).asSlider().getValue(), (int) this.getSettings().get(2).asSlider().getValue())).getRGB();
        String s = "Ruhama+ " + RuhamaPlus.version;
        if (this.getSettings().get(7).asToggle().state) this.getWindows().get(0).addText(new AdvancedText(s, true, color));

        if (this.getSettings().get(3).asToggle().state)
        {
            int age = (int) (System.currentTimeMillis() / 20L % 510L);
            color = (new Color(255, MathHelper.clamp(age > 255 ? 510 - age : age, 0, 255), MathHelper.clamp(255 - (age > 255 ? 510 - age : age), 0, 255))).getRGB();
        } else
        {
            color = (new Color((int) this.getSettings().get(4).asSlider().getValue(), (int) this.getSettings().get(5).asSlider().getValue(), (int) this.getSettings().get(6).asSlider().getValue())).getRGB();
        }

        List<Module> arrayList = ModuleManager.getModules();

        arrayList.remove(this);
        arrayList.remove(ClickGui.class);
        arrayList.remove(Closest.class);
        arrayList.remove(Hud.class);
        arrayList.remove(PvpInfo.class);

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
