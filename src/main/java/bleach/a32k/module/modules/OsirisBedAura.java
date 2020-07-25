package bleach.a32k.module.modules;

import bleach.a32k.module.Category;
import bleach.a32k.module.Module;
import bleach.a32k.settings.SettingBase;
import bleach.a32k.settings.SettingToggle;
import bleach.a32k.settings.SettingSlider;
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

    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 6.0D, 4.5D, 0, "Range: "), new SettingToggle(false, "Rotate"), new SettingToggle(true, "DimensionCheck"), new SettingToggle(true, "Refill Hotbar"));

    public OsirisBedAura() {
        super("OsirisBedAura", 0, Category.FYREHACK, "OsirisBedAura", settings);
    }

    boolean moving = false;



    public void onUpdate() {
        if (this.getSettings().get(3).toToggle().state) {
            //search for empty hotbar slots
            int slot = -1;
            for (int i = 0; i < 9; i++) {
                if (mc.player.inventory.getStackInSlot(i) == ItemStack.EMPTY) {
                    slot = i;
                    break;
                }
            }
            if (moving && slot != -1) {
                mc.playerController.windowClick(0, slot + 36, 0, ClickType.PICKUP, mc.player);
                moving = false;
                slot = -1;
            }

            if (slot != -1 && !(mc.currentScreen instanceof GuiContainer) && mc.player.inventory.getItemStack().isEmpty()) {
                //search for beds in inventory
                int t = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED && i >= 9) {
                        t = i;
                        break;
                    }
                }

                //click bed item
                if (t != -1) {
                    mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, mc.player);
                    moving = true;
                }
            }
        }
        mc.world.loadedTileEntityList.stream()
                .filter(e -> e instanceof TileEntityBed)
                .filter(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <=  this.getSettings().get(11).toSlider().getValue())
                .sorted(Comparator.comparing(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ())))
                .forEach(bed -> {

                    if (this.getSettings().get(2).toToggle().state && mc.player.dimension == 0) return;


                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));

        });
    }
}

