package blu3.FyreHack.module.modules.misc;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.settings.SettingToggle;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ElytraReplace extends Module
{

    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(1.0D, 30.0D, 9.0D, 0, "Replace Threshold: "));

    public ElytraReplace()
    {
        super("ElytraReplace", 0, Category.MISC, "Automatically replaces your elytra when its low", settings);
    }

    public void onUpdate()
    {
        if (this.mc.player.inventoryContainer.getSlot(6).getStack().getItem() instanceof ItemElytra && this.mc.player.inventoryContainer.getSlot(6).getStack().getMaxDamage() - this.mc.player.inventoryContainer.getSlot(6).getStack().getItemDamage() < 9)
        {
            int i = 9;

            for (byte n = 9; i <= 44; i = ++n)
            {
                ItemStack stack;

                if ((stack = this.mc.player.inventoryContainer.getSlot(n).getStack()) != ItemStack.EMPTY && stack.getItem() instanceof ItemElytra && stack.getCount() == 1 && stack.getMaxDamage() - stack.getItemDamage() > (this.getSettings().get(0).toSlider().getValue()))
                {
                    this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, this.mc.player);
                    this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, n, 0, ClickType.QUICK_MOVE, this.mc.player);
                    this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, n, 0, ClickType.PICKUP, this.mc.player);
                }
            }
        }
    }
}
