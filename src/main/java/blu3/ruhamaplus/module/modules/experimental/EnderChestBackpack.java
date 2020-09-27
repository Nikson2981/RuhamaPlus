package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;


public class EnderChestBackpack extends Module {

    private GuiScreen echestScreen;

    public EnderChestBackpack()
    {
        super("EnderChestBackpack", 0, Category.EXPERIMENTAL, "heehoo how no place", null);
        this.echestScreen = null;
    }


    public void onUpdate()
    {
        if ((this.mc.currentScreen instanceof GuiContainer))
        {
            Container container = ((GuiContainer)this.mc.currentScreen).inventorySlots;
            if (((container instanceof ContainerChest)) && ((((ContainerChest)container).getLowerChestInventory() instanceof InventoryBasic)))
            {
                InventoryBasic basic = (InventoryBasic)((ContainerChest)container).getLowerChestInventory();
                if (basic.getName().equalsIgnoreCase("Ender Chest"))
                {
                    this.echestScreen = this.mc.currentScreen;
                    this.mc.currentScreen = null;
                }
            }
        }
    }

    public void onDisable()
    {
        if ((this.echestScreen != null) && (this.mc.player != null) && (this.mc.world != null)) {
            this.mc.displayGuiScreen(this.echestScreen);
        }
        this.echestScreen = null;
    }
}
