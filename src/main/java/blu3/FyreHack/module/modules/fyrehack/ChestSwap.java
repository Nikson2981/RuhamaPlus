package blu3.FyreHack.module.modules.fyrehack;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingMode;
import blu3.FyreHack.settings.SettingToggle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ChestSwap extends Module {

    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Prefer Elytra"), new SettingToggle(true, "Curse"));
    public ChestSwap()
    {
        super("ChestSwap", 0, Category.FYREHACK, "swap chestplate ok", settings);
    }

    private boolean Curse = this.getSettings().get(1).toToggle().state;
    private boolean PreferElytra = this.getSettings().get(2).toToggle().state;

    @Override
    public void onEnable() {
        if (this.mc.player == null) return;
        ItemStack l_ChestSlot = mc.player.inventoryContainer.getSlot(6).getStack();
        if (l_ChestSlot.isEmpty())
        {
            int l_Slot = FindChestItem(PreferElytra);

            if (!PreferElytra && l_Slot == -1)
                l_Slot = FindChestItem(true);

            if (l_Slot != -1)
            {
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
            }

            toggle();
            return;
        }

        int l_Slot = FindChestItem(l_ChestSlot.getItem() instanceof ItemArmor);

        if (l_Slot != -1)
        {
            this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
            this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, mc.player);
        }

        this.setToggled(false);
    }
    private int FindChestItem(boolean p_Elytra)
    {
        int slot = -1;
        float damage = 0;

        for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i)
        {
            /// @see: https://wiki.vg/Inventory, 0 is crafting slot, and 5,6,7,8 are Armor slots
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8)
                continue;

            ItemStack s = mc.player.inventoryContainer.getInventory().get(i);
            if (s != null && s.getItem() != Items.AIR)
            {
                if (s.getItem() instanceof ItemArmor)
                {
                    final ItemArmor armor = (ItemArmor) s.getItem();
                    if (armor.armorType == EntityEquipmentSlot.CHEST)
                    {
                        final float currentDamage = (armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, s));

                        final boolean cursed = Curse ? (EnchantmentHelper.hasBindingCurse(s)) : false;

                        if (currentDamage > damage && !cursed)
                        {
                            damage = currentDamage;
                            slot = i;
                        }
                    }
                }
                else if (p_Elytra && s.getItem() instanceof ItemElytra)
                    return i;
            }
        }

        return slot;
    }



}
