package blu3.FyreHack.module.modules.fyrehack;


import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.settings.SettingToggle;
import blu3.FyreHack.utils.RenderUtils;
import blu3.FyreHack.utils.FyreLogger;
import blu3.FyreHack.utils.WorldUtils;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//
// made by blu3
//

public class BedAuraECME extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Rotate (required)"),  new SettingSlider(0.0D, 20.0D, 20.0D, 0, "Delay: "), new SettingSlider(0.0D, 7.0D, 5.0D, 0, "PlayerRange: "), new SettingToggle(false, "Debug Messages"), new SettingToggle(false, "Fill Hotbar"),  new SettingSlider(0.0D, 7.0D, 5.0D, 0, "BedRange"));
// 0 = rotate, 1= delay, 2= range, 3 = debug, 4 = autoswitch, 5 = bedrange

    private BlockPos blockpos1;
    private BlockPos blockpos2;
    private BlockPos blockpos3;
    private BlockPos blockpos4;
    private BlockPos blockpos5;
    private BlockPos blockpos6;
    private BlockPos blockpos7;
    private BlockPos blockpos8;
    private BlockPos blockpos9;
    private BlockPos blockpos10;
    private BlockPos blockpos11;
    private BlockPos blockpos12;
    private BlockPos blockpos13;
    private BlockPos blockpos14;

    private BlockPos render;

    private EntityPlayer target;

    public BedAuraECME() { super("BedAura", 0, Category.FYREHACK, "1.13 BedAura", settings); }

    public boolean isInBlockRange(Entity target)
    {
        return target.getDistance(this.mc.player) <= 4.0F;
    }

    public boolean isValid(EntityPlayer entity)
    {
        return entity != null && this.isInBlockRange(entity) && entity.getHealth() > 0.0F && !entity.isDead;
    }


    boolean moving = false;

    double delay;
    public void onUpdate() {


        //click any nearby beds
        this.clickBed();



        if (!this.mc.player.isHandActive()) {
            if (!this.isValid(this.target) || this.target == null) {
                this.updateTarget();
            }

            Iterator<EntityPlayer> playerIter = this.mc.world.playerEntities.iterator();

            EntityPlayer player;

            do {
                if (!playerIter.hasNext()) {
                    if (this.isValid(this.target) && this.mc.player.getDistance(this.target) < this.getSettings().get(2).toSlider().getValue()) {
                        this.render = null;

                        this.delay++;

                        if (this.delay >= this.getSettings().get(1).toSlider().getValue()) {
                            this.trap(this.target);
                            this.delay = 0;

                        }
                    }
                    return;
                }

                player = playerIter.next();
            } while (player instanceof EntityPlayerSP || !this.isValid(player) || player.getDistance(this.mc.player) >= this.target.getDistance(this.mc.player));

            this.target = player;
        }

    }


    private void trap(EntityPlayer player) {

        this.blockpos1 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ); // above player head
        this.blockpos2 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ); // player head
        this.blockpos3 = new BlockPos(player.posX + 1.0D, player.posY, player.posZ); // +x surround
        this.blockpos4 = new BlockPos(player.posX, player.posY, player.posZ + 1.0D); // +z surround
        this.blockpos5 = new BlockPos(player.posX - 1.0D, player.posY, player.posZ); // -x surround
        this.blockpos6 = new BlockPos(player.posX, player.posY, player.posZ - 1.0D); // -z surround
        this.blockpos7 = new BlockPos(player.posX + 1.0D, player.posY + 1.0D, player.posZ); // +x bed
        this.blockpos8 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ + 1.0D); // +z bed
        this.blockpos9 = new BlockPos(player.posX - 1.0D, player.posY + 1.0D, player.posZ); // -x bed
        this.blockpos10 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ - 1.0D); // -z bed

        this.blockpos11 = new BlockPos(player.posX + 1.0D, player.posY + 2.0D, player.posZ); // +x upper bed
        this.blockpos12 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ + 1.0D); // +z upper bed
        this.blockpos13 = new BlockPos(player.posX - 1.0D, player.posY + 2.0D, player.posZ); // -x upper bed
        this.blockpos14 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ - 1.0D); // -z upper bed


        // +x bed

        if (this.mc.player.getDistance(player.posX + 1.0D, player.posY + 1.0D, player.posZ) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos7).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX - 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);

                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place +x");
                    }
                    this.render = blockpos3;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos7, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

                }
            }
        }
        // +z bed

        if (this.mc.player.getDistance(player.posX, player.posY + 1.0D, player.posZ + 1.0D) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos8).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ - 2.0D);


                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place +z");
                    }

                    this.render = blockpos4;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos8, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

                }
            }
        }

        // -x bed

        if (this.mc.player.getDistance(player.posX - 1.0D, player.posY + 1.0D, player.posZ) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos9).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX + 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);

                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place -x");
                    }
                    this.render = blockpos5;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos9, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                }
            }
        }
        // -z bed

        if (this.mc.player.getDistance(player.posX, player.posY + 1.0D, player.posZ - 1.0D) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos10).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ + 2);

                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place -x");
                    }
                    this.render = blockpos6;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos10, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                }
            }
        }

        // +x upper bed

        if (this.mc.player.getDistance(player.posX + 1.0D, player.posY + 2.0D, player.posZ) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos11).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX - 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);

                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place +x");
                    }
                    this.render = blockpos3;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos11, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

                }
            }
        }

        // +z upper bed
        if (this.mc.player.getDistance(player.posX, player.posY + 2.0D, player.posZ + 1.0D) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos12).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ - 2.0D);


                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place +z");
                    }

                    this.render = blockpos4;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos12, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

                }
            }
        }

        // -x upper bed
        if (this.mc.player.getDistance(player.posX - 1.0D, player.posY + 2.0D, player.posZ) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos13).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX + 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ);

                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place -x");
                    }
                    this.render = blockpos5;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos13, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                }
            }
        }
        // -z upper bed
        if (this.mc.player.getDistance(player.posX, player.posY + 2.0D, player.posZ - 1.0D) <= this.getSettings().get(5).toSlider().getValue()) {
            if (this.mc.world.getBlockState(this.blockpos14).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable()) {

                    WorldUtils.rotatePacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ + 2);

                    if (this.getSettings().get(3).toToggle().state) {
                        FyreLogger.log("attempting to place -x");
                    }
                    this.render = blockpos6;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos14, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                }
            }
        }
    }



    public void onEnable(){
        FyreLogger.log("BedAura:" + TextFormatting.GREEN + " ENABLED!");
    }
    public void onDisable() {
        FyreLogger.log("BedAura:" + TextFormatting.RED + " DISABLED!");
        this.target = null;
    }

    public void updateTarget()
    {
        for (EntityPlayer player : this.mc.world.playerEntities)
        {
            if (!(player instanceof EntityPlayerSP) && this.isValid(player))
            {
                this.target = player;
            }
        }
    }

    public void onRender()
    {
        if (this.render != null)
        {
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(this.render), 1.0F, 1.0F, 1.0F, 0.3F);
        }
    }

    public void clickBed(){

        if (this.getSettings().get(4).toToggle().state) {

            if (mc.currentScreen instanceof GuiContainer) return;

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
                    if (mc.player.inventory.getStackInSlot(slot).isEmpty())
                        mc.playerController.windowClick(0, t, 0, ClickType.QUICK_MOVE, mc.player);
                }
            }
        }

        int x;

        double range = this.getSettings().get(5).toSlider().getValue();
        double negativeRange = range - range * 2;

        for (x = (int) negativeRange; x <= range; ++x)
        {
            for (int y = (int) negativeRange; y <= range; ++y)
            {
                for (int z = (int) negativeRange; z <= range; ++z)
                {
                    BlockPos pos = this.mc.player.getPosition().add(x, y, z);

                    if (this.mc.world.getBlockState(pos).getBlock() instanceof BlockBed && this.mc.player.getPositionVector().distanceTo((new Vec3d(pos)).add(0.5D, 0.5D, 0.5D)) <= 5.25D)
                    {
                        this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock (pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
                    }
                }
            }
        }
    }
}
