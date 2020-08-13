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
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Hud extends Module {

    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Watermark"), new SettingToggle(true, "ArrayList"), new SettingToggle(true, "ServerIP"));

    public Hud() {super("Hud", Keyboard.KEY_NONE, Category.RENDER, "Shows stuff onscreen.", settings);}

    public int height = 2;

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public static int rgb;

    @SubscribeEvent
    public void onTick(TickEvent event) {
        rgb+=1;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        height = 2;

        if (this.getSettings().get(0).toToggle().state) {
            mc.fontRenderer.drawString("Fyre Hack 0.2", 2, height, Rainbow.getInt());
            height+=10;
        }

        if(this.getSettings().get(2).toToggle().state) {
            if (mc.getCurrentServerData() != null && !this.mc.getCurrentServerData().serverIP.equals("")) {
                mc.fontRenderer.drawString("IP: " + this.mc.getCurrentServerData().serverIP, 2, height, Rainbow.getInt());
            }
            if (this.mc.isIntegratedServerRunning()) {
                mc.fontRenderer.drawString("IP: Singleplayer", 2, height, Rainbow.getInt());
            }

            height+=10;
        }
        for (Module m: ModuleManager.getModules()) if (m.isToggled() && this.getSettings().get(1).toToggle().state) {
            if (m.getName() == "Hud") return;
            mc.fontRenderer.drawString(m.getName(), 2, height, Rainbow.getInt());
            height+=10;
        }
    }
}