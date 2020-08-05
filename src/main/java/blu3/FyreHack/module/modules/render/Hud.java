package blu3.FyreHack.module.modules.render;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.module.ModuleManager;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingToggle;
import blu3.FyreHack.utils.Rainbow;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Hud extends Module {

    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Watermark"), new SettingToggle(true, "ArrayList"));

    public Hud() {super("Hud", Keyboard.KEY_NONE, Category.RENDER, "Shows stuff onscreen.", settings);}

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        int height = 2;

        if (this.getSettings().get(0).toToggle().state) {
            mc.fontRenderer.drawString("Fyre Hack 0.2", 2, height, Rainbow.getInt());
            height+=10;
        }

        for (Module m: ModuleManager.getModules()) if (m.isToggled() && this.getSettings().get(1).toToggle().state) {
            if (m.getName() == "Hud") return;
            mc.fontRenderer.drawString(m.getName(), 2, height, Rainbow.getInt());
            height+=10;
        }
    }
}