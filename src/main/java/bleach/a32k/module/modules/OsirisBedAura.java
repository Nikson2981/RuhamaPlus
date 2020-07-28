package bleach.a32k.module.modules;

import bleach.a32k.module.Category;
import bleach.a32k.module.Module;
import bleach.a32k.settings.SettingBase;
import bleach.a32k.settings.SettingToggle;
import bleach.a32k.settings.SettingSlider;
import bleach.a32k.utils.RuhamaLogger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OsirisBedAura extends Module{

    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 6.0D, 4.5D, 0, "Range: "), new SettingToggle(false, "Rotate"), new SettingToggle(true, "DimensionCheck"), new SettingToggle(true, "Refill Hotbar"),new SettingToggle(false, "Debug Messages"), new SettingToggle(true, "Toggle Messages"));

    public OsirisBedAura() {
        super("HotbarBeds", 0, Category.FYREHACK, "OsirisBedAura", settings);
    }

    boolean moving = false;



    public void onUpdate() {
        if (this.getSettings().get(3).toToggle().state) {
            //search for empty hotbar slots
            int slot = -1;
            for (int i = 0; i < 9; i++) {
                if (this.mc.player.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
                    slot = i;
                    break;
                }
            }
            if (moving && slot != -1) {
                this.mc.playerController.windowClick(0, slot + 36, 0, ClickType.PICKUP, this.mc.player);
                this.moving = false;
                slot = -1;
            }

            if (slot != -1 && !(this.mc.currentScreen instanceof GuiContainer) && this.mc.player.inventory.getItemStack().isEmpty()) {
                //search for beds in inventory
                int t = -1;
                for (int i = 0; i < 45; i++) {
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED && i >= 9) {
                        t = i;
                        break;
                    }
                }

                //click bed item
                if (t != -1) {
                    this.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, this.mc.player);
                    this.moving = true;
                }
            }
        }
    }
    public void onEnable(){
        if (this.getSettings().get(5).toToggle().state){
            RuhamaLogger.log("OsirisBedAura: ENABLED");
        }
    }

    public void onDisable(){
        if (this.getSettings().get(5).toToggle().state){
            RuhamaLogger.log("OsirisBedAura: DISABLED");
        }
    }
}

