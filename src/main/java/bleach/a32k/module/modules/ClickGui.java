package bleach.a32k.module.modules;

import bleach.a32k.gui.NewFyreGui;
import bleach.a32k.module.Category;
import bleach.a32k.module.Module;

public class ClickGui extends Module
{
    public static NewFyreGui clickGui = new NewFyreGui();

    public ClickGui()
    {
        super("ClickGui", 0, Category.RENDER, "Clickgui", null);
    }

    public void onEnable()
    {
        this.mc.displayGuiScreen(clickGui);
        this.setToggled(false);
    }
}
