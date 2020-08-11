package blu3.FyreHack.module.modules.render;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class FogColour extends Module {
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0, 100, 0, 1, "Red: "), new SettingSlider(0, 100, 0, 1, "Green: "), new SettingSlider(0, 100, 0, 1, "Blue: "));

    public FogColour() {super("FogColour",0,Category.RENDER, "Changes fog colour",settings);}

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void fogColour(EntityViewRenderEvent.FogColors event) {
        event.setRed((float)this.getSettings().get(0).toSlider().getValue()/100);
        event.setGreen((float)this.getSettings().get(1).toSlider().getValue()/100);
        event.setBlue((float)this.getSettings().get(2).toSlider().getValue()/100);
    }
}
