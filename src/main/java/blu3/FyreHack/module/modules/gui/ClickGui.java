package blu3.FyreHack.module.modules.gui;

import blu3.FyreHack.gui.NewFyreGui;
import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;

public class ClickGui extends Module
{
    public static NewFyreGui clickGui = new NewFyreGui();

    public ClickGui()
    {
        super("ClickGui", 0, Category.GUI, "Clickgui", null);
    }

    public void onEnable()
    {
        this.mc.displayGuiScreen(clickGui);
        this.setToggled(false);
    }
}
