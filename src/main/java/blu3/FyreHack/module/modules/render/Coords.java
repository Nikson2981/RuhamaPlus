package blu3.FyreHack.module.modules.render;


import blu3.FyreHack.gui.AdvancedText;
import blu3.FyreHack.gui.TextWindow;
import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class Coords extends Module {

    public Coords()
    {
        super("Coords", 0, Category.RENDER, "Shows the closest player to you", null);

        this.getWindows().add(new TextWindow(2, 150, "Coordinates"));
    }

    public int x = (int) this.mc.player.posX;
    public int y = (int) this.mc.player.posY;
    public int z = (int) this.mc.player.posZ;

    public void onOverlay(){
        this.getWindows().get(0).clearText();


        int age = (int) (System.currentTimeMillis() / 20L % 510L);
        int color = (new Color(255, MathHelper.clamp(age > 255 ? 510 - age : age, 0, 255), MathHelper.clamp(255 - (age > 255 ? 510 - age : age), 0, 255))).getRGB();

        this.getWindows().get(0).addText(new AdvancedText(x + ", " + y + ", " + z, false, color));
    }

}
