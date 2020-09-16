package blu3.ruhamaplus.module.modules.combat;


import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.WorldUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 **  @author blu3 LMAO
 **/

public class BedAuraECME extends Module {
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(0.0D, 20.0D, 4.0D, 0, "BreakDelay: "), new SettingSlider(0.0D, 20.0D, 16.0D, 0, "PlaceDelay: "), new SettingSlider(0.0D, 7.0D, 5.0D, 0, "PlayerRange: "), new SettingToggle(false, "Debug Messages"), new SettingToggle(true, "Fill Hotbar"), new SettingSlider(0.0D, 7.0D, 5.0D, 0, "PlaceRange"), new SettingToggle(true, "Place"), new SettingToggle(true, "Explode"));
// 0 = rotate, 1= delay, 2= range, 3 = debug, 4 = hotbar, 5 = bedrange, 6 = place, 7 = explode

    private BlockPos blockpos1;
    private BlockPos blockpos2;
    private BlockPos blockpos7;
    private BlockPos blockpos8;
    private BlockPos blockpos9;
    private BlockPos blockpos10;
    private BlockPos blockpos11;
    private BlockPos blockpos12;
    private BlockPos blockpos13;
    private BlockPos blockpos14;


    private final List<BlockPos> beds = new ArrayList<>();

    private EntityPlayer target;

    public BedAuraECME() {
        super("BedAura", 0, Category.COMBAT, "1.13 BedAura", settings);

    }


    //if (mc.player.ticksExisted % delay.getValue() == 0) {
    //            this.update();
    //        }

    public boolean isInBlockRange(Entity target) {
        return target.getDistance(this.mc.player) <= 8.0F;
    }

    public boolean isValid(EntityPlayer entity) {
        return entity != null && this.isInBlockRange(entity) && entity.getHealth() > 0.0F && !entity.isDead;
    }



    public void onUpdate() {
        this.renderBeds();
        if (!this.mc.player.isHandActive()) {
            if (!this.isValid(this.target) || this.target == null) {
                this.updateTarget();
            }

            List<Entity> entities = new ArrayList<>();

            entities.addAll(this.mc.world.playerEntities);
            for (Object o : new ArrayList<>(entities)) {
                Entity e = (EntityPlayer) o;

                if (FriendManager.Get().isFriend(e.getName().toLowerCase())){
                    entities.remove(e);
                }
            }
            Iterator<Entity> entityIter = entities.iterator();

            EntityPlayer player;




            if (mc.player.ticksExisted % this.getSetting(0).asSlider().getValue() == 0) {
                if (this.getSetting(7).asToggle().state) this.clickBed();
            }

            if (mc.player.ticksExisted % this.getSetting(1).asSlider().getValue() == 0) {
                this.moveBed();

                do {

                    if (!entityIter.hasNext()) {
                        if (this.isValid(this.target) && this.mc.player.getDistance(this.target) < this.getSetting(2).asSlider().getValue()) {
                            if (this.mc.player.getHeldItemMainhand().getItem() == Items.BED) {
                                if (this.getSetting(6).asToggle().state) this.bomb(this.target);
                            }
                        }
                        return;
                    }
                    player = (EntityPlayer) entityIter.next();


                } while (player instanceof EntityPlayerSP || !this.isValid(player) || player.getDistance(this.mc.player) >= this.target.getDistance(this.mc.player));

                this.target = player;
            }
        }
    }

    public void onEnable() {
        ClientChat.log("BedAura:" + TextFormatting.GREEN + " ENABLED!");
    }

    public void onDisable() {
        ClientChat.log("BedAura:" + TextFormatting.RED + " DISABLED!");
        this.target = null;
        this.beds.clear();
    }

    private void bomb(EntityPlayer player) {
    //
    // ew
    //
            this.blockpos1 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ); // above player head
            this.blockpos2 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ); // player head
            this.blockpos7 = new BlockPos(player.posX + 1.0D, player.posY + 1.0D, player.posZ); // +x bed
            this.blockpos8 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ + 1.0D); // +z bed
            this.blockpos9 = new BlockPos(player.posX - 1.0D, player.posY + 1.0D, player.posZ); // -x bed
            this.blockpos10 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ - 1.0D); // -z bed
            this.blockpos11 = new BlockPos(player.posX + 1.0D, player.posY + 2.0D, player.posZ); // +x upper bed
            this.blockpos12 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ + 1.0D); // +z upper bed
            this.blockpos13 = new BlockPos(player.posX - 1.0D, player.posY + 2.0D, player.posZ); // -x upper bed
            this.blockpos14 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ - 1.0D); // -z upper bed

            // +x bed

            if (this.mc.player.getDistance(player.posX + 1.0D, player.posY + 1.0D, player.posZ) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos7).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                        WorldUtils.rotatePacket(this.mc.player.posX - 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);


                        this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos7, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                    }
                }
            }
            // +z bed

            if (this.mc.player.getDistance(player.posX, player.posY + 1.0D, player.posZ + 1.0D) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos8).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                        WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ - 2.0D);


                        this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos8, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                    }
                }
            }

            // -x bed

            if (this.mc.player.getDistance(player.posX - 1.0D, player.posY + 1.0D, player.posZ) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos9).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                        WorldUtils.rotatePacket(this.mc.player.posX + 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);


                        this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos9, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                    }
                }
            }
            // -z bed

            if (this.mc.player.getDistance(player.posX, player.posY + 1.0D, player.posZ - 1.0D) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos10).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                        WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ + 2);


                        this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos10, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                    }
                }
            }

            // +x upper bed

            if (this.mc.player.getDistance(player.posX + 1.0D, player.posY + 2.0D, player.posZ) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos11).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {
                        if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                            WorldUtils.rotatePacket(this.mc.player.posX - 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);


                            this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos11, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                        }
                    }
                }
            }

            // +z upper bed
            if (this.mc.player.getDistance(player.posX, player.posY + 2.0D, player.posZ + 1.0D) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos12).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {
                        if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                            WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ - 2.0D);


                            this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos12, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                        }
                    }
                }
            }

            // -x upper bed
            if (this.mc.player.getDistance(player.posX - 1.0D, player.posY + 2.0D, player.posZ) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos13).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {
                        if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                            WorldUtils.rotatePacket(this.mc.player.posX + 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);


                            this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos13, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                        }
                    }
                }
            }
            // -z upper bed
            if (this.mc.player.getDistance(player.posX, player.posY + 2.0D, player.posZ - 1.0D) <= this.getSetting(5).asSlider().getValue()) {
                if (this.mc.world.getBlockState(this.blockpos14).getMaterial().isReplaceable()) {
                    if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {
                        if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                            WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ + 2);


                            this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos14, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                        }
                    }
                }
            }
        }



    public void updateTarget() {
        for (EntityPlayer player : this.mc.world.playerEntities) {
            if (!(player instanceof EntityPlayerSP) && this.isValid(player)) {
                this.target = player;
            }
        }
    }

    public void onRender()
    {
        float blue = (float) (System.currentTimeMillis() / 10L % 512L) / 255.0F;
        float red = (float) (System.currentTimeMillis() / 16L % 512L) / 255.0F;

        if (blue > 1.0F)
        {
            blue = 1.0F - blue;
        }

        if (red > 1.0F)
        {
            red = 1.0F - red;
        }

        for (BlockPos p : this.beds)
        {
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(p), red, 0.7F, blue, 0.25F);
        }

    }

    public void renderBeds(){

        this.beds.clear();

        int x;

        if (!(mc.player.dimension == 0)) {

            double range = this.getSetting(5).asSlider().getValue();
            double negativeRange = range - range * 2;

            for (x = (int) negativeRange; x <= range; ++x) {
                for (int y = (int) negativeRange; y <= range; ++y) {
                    for (int z = (int) negativeRange; z <= range; ++z) {
                        BlockPos pos = this.mc.player.getPosition().add(x, y, z);

                        if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockBed && this.mc.player.getPositionVector().distanceTo((new Vec3d(pos)).add(0.5D, 0.5D, 0.5D)) <= 5.25D) {
                            this.beds.add(pos);
                        }
                    }
                }
            }
        }
    }

    public void clickBed() {

        int x;

        if (!(mc.player.dimension == 0)) {

            double range = this.getSetting(5).asSlider().getValue();
            double negativeRange = range - range * 2;

            for (x = (int) negativeRange; x <= range; ++x) {
                for (int y = (int) negativeRange; y <= range; ++y) {
                    for (int z = (int) negativeRange; z <= range; ++z) {
                        BlockPos pos = this.mc.player.getPosition().add(x, y, z);

                        if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockBed && this.mc.player.getPositionVector().distanceTo((new Vec3d(pos)).add(0.5D, 0.5D, 0.5D)) <= 5.25D) {
                            WorldUtils.openBlock(pos);
                        }
                    }
                }
            }
        }
    }

    public void moveBed(){
        if (this.getSetting(4).asToggle().state) {

            if (mc.currentScreen instanceof GuiContainer) return;

            if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBed) return;

            for (int i = 0; i <= 9; i++)
                if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                    return;
                }

            int slot = -1;

            for (int i = 0; i <= 9; i++)
                if (mc.player.inventory.getStackInSlot(i).isEmpty() && slot == -1) {
                    slot = i;
                    break;
                }

            if (slot != -1 && this.mc.player.inventory.getItemStack().isEmpty()) {
                int t = -1;
                for (int i = 0; i <= 46; i++)
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED && i > 9) {
                        t = i;
                        break;
                    }

                if (t != 1) {
                    if (mc.player.inventory.getStackInSlot(slot).isEmpty()) {
                        mc.playerController.windowClick(0, t, 0, ClickType.QUICK_MOVE, mc.player);
                    }
                }
            }
        }
    }

}
