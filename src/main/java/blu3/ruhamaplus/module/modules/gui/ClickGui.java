package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.gui.NewRuhamaGui;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;

public class ClickGui extends Module
{
    public static NewRuhamaGui clickGui = new NewRuhamaGui();

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
