package blu3.FyreHack.module.modules.gui;

import blu3.FyreHack.FyreHack;
import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.module.ModuleManager;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.settings.SettingToggle;
import blu3.FyreHack.utils.Rainbow;
import com.mojang.realmsclient.client.Ping;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Hud extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "Watermark"),
            new SettingToggle(true, "ServerIP"),
            new SettingToggle(true, "Ping"),
            new SettingToggle(true, "Username"),
            new SettingToggle(true, "Dimension"));

    public Hud() {super("Hud", Keyboard.KEY_NONE, Category.GUI, "Shows stuff onscreen.", settings);}

    public int getPing() {
        int p = -1;
        if (this.mc.player == null || this.mc.getConnection() == null || this.mc.getConnection().getPlayerInfo(this.mc.player.getName()) == null) {
            p = -1;
        }
        else {
            p = this.mc.getConnection().getPlayerInfo(this.mc.player.getName()).getResponseTime();
        }
        return p;
    }

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

        if(this.getSettings().get(1).toToggle().state) {
            if (mc.getCurrentServerData() != null && !this.mc.getCurrentServerData().serverIP.equals("")) {
                mc.fontRenderer.drawString("IP: " + this.mc.getCurrentServerData().serverIP, 2, height, Rainbow.getInt());
            }
            if (this.mc.isIntegratedServerRunning()) {
                mc.fontRenderer.drawString("IP: Singleplayer", 2, height, Rainbow.getInt());
            }
            if (this.mc.getCurrentServerData() == null) {
                mc.fontRenderer.drawString("IP: ERROR", 2, height, Rainbow.getInt());
            }
            height+=10;
        }

        if (this.getSettings().get(2).toToggle().state) {
            mc.fontRenderer.drawString("Ping: " + this.getPing() + "ms", 2, height, Rainbow.getInt());
            height+=10;
        }

        if (this.getSettings().get(3).toToggle().state) {
            mc.fontRenderer.drawString("Logged in as " + this.mc.player.getName(), 2, height, Rainbow.getInt());
            height+=10;
        }
        if (this.getSettings().get(4).toToggle().state) {

            final int s = this.mc.player.dimension;
            String biom = "";

            if (s == 0) {
                biom = "Dimension: Overworld";
            }
            else if (s == -1) {
                biom = "Dimension: Nether";
            }
            else if (s == 1) {
                biom = "Dimension: End";
            }
            mc.fontRenderer.drawString(biom, 2, height, Rainbow.getInt());
            height+=10;
        }

    }

}