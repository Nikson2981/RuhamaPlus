package bleach.a32k.module.modules;

import bleach.a32k.Ruhama;
import bleach.a32k.module.Category;
import bleach.a32k.module.Module;
import bleach.a32k.settings.SettingBase;
import bleach.a32k.settings.SettingSlider;
import bleach.a32k.settings.SettingToggle;
import bleach.a32k.utils.RenderUtils;
import bleach.a32k.utils.RuhamaLogger;
import bleach.a32k.utils.WorldUtils;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AutoBedCity extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "Rotate (required)"),  new SettingSlider(0.0D, 20.0D, 20.0D, 0, "Delay: "), new SettingSlider(0.0D, 7.0D, 5.0D, 0, "PlayerRange: "), new SettingToggle(false, "Debug Messages"), new SettingToggle(false, "AutoSwitch"),  new SettingSlider(0.0D, 7.0D, 5.0D, 0, "BedRange"));
// 0 = rotate, 1= delay, 2= range, 3 = debug, 4 = autoswitch, 5 = bedrange

    private BlockPos blockpos2;
    private BlockPos blockpos3;
    private BlockPos blockpos4;
    private BlockPos blockpos5;
    private BlockPos blockpos6;
    private BlockPos blockpos7;
    private BlockPos blockpos8;
    private BlockPos blockpos9;
    private BlockPos blockpos10;

    private BlockPos render;

    private boolean isSpoofingAngles;

    int bedSlot;

    private boolean placed;

    private EntityPlayer target;

    public AutoBedCity() { super("AutoBedCity", 0, Category.FYREHACK, "cities people with beds automatically", settings); }

    public boolean isInBlockRange(Entity target)
    {
        return target.getDistance(this.mc.player) <= 4.0F;
    }

    public boolean isValid(EntityPlayer entity)
    {
        return entity != null && this.isInBlockRange(entity) && entity.getHealth() > 0.0F && !entity.isDead;
    }





    public void onUpdate()
    {

        if (this.getSettings().get(4).toToggle().state) {
            bedSlot = this.mc.player.getHeldItemMainhand().getItem() == Items.BED ? this.mc.player.inventory.currentItem : -1;

            if (bedSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (this.mc.player.inventory.getStackInSlot(l).getItem() == Items.BED) {
                        bedSlot = l;
                        if (this.getSettings().get(3).toToggle().state) {
                            RuhamaLogger.log("set bedSlot");
                        }
                        break;
                    }
                }
            }
        }

        this.clickBed();
        this.placed = false;

        if (!this.mc.player.isHandActive())
        {
            if (!this.isValid(this.target) || this.target == null)
            {
                this.updateTarget();
            }

            Iterator<EntityPlayer> playerIter = this.mc.world.playerEntities.iterator();

            EntityPlayer player;

            do
            {
                if (!playerIter.hasNext())
                {
                    if (this.isValid(this.target) && this.mc.player.getDistance(this.target) < this.getSettings().get(2).toSlider().getValue())
                    {
                        if (this.getSettings().get(3).toToggle().state)
                        {
                            RuhamaLogger.log("found valid target, trying to city");
                        }
                        this.trap(this.target);
                        this.render = null;
                    }

                    return;
                }

                player = playerIter.next();
            } while (player instanceof EntityPlayerSP || !this.isValid(player) || player.getDistance(this.mc.player) >= this.target.getDistance(this.mc.player));

            this.target = player;
        }
    }

    private void trap(EntityPlayer player) {

        this.blockpos2 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ); // player head
        this.blockpos3 = new BlockPos(player.posX + 1.0D, player.posY, player.posZ); // +x surround
        this.blockpos4 = new BlockPos(player.posX, player.posY, player.posZ + 1.0D); // +z surround
        this.blockpos5 = new BlockPos(player.posX - 1.0D, player.posY, player.posZ); // -x surround
        this.blockpos6 = new BlockPos(player.posX, player.posY, player.posZ - 1.0D); // -z surround
        this.blockpos7 = new BlockPos(player.posX + 1.0D, player.posY + 1.0D, player.posZ); // +x bed
        this.blockpos8 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ + 1.0D); // +z bed
        this.blockpos9 = new BlockPos(player.posX - 1.0D, player.posY + 1.0D, player.posZ); // -x bed
        this.blockpos10 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ - 1.0D); // -z bed


        if (this.getSettings().get(3).toToggle().state) {
            RuhamaLogger.log("set block positions");
        }


        if (this.getSettings().get(4).toToggle().state) {
            this.mc.player.inventory.currentItem = bedSlot;
            if (this.getSettings().get(3).toToggle().state) {
                RuhamaLogger.log("switched to bed");
            }
        }

        // +x bed

            if (this.mc.world.getBlockState(this.blockpos7).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    this.lookAtPacket(this.mc.player.posX - 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ, this.mc.player);

                    if (this.getSettings().get(3).toToggle().state) {
                        RuhamaLogger.log("attempting to place +x");
                    }
                    this.render = blockpos3;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos7, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));

                    if (this.getSettings().get(0).toToggle().state) {
                        this.resetRotation();
                    }


            }
        }

        // +z bed

            if (this.mc.world.getBlockState(this.blockpos8).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {
                    this.lookAtPacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ - 2.0D, this.mc.player);

                    if (this.getSettings().get(3).toToggle().state) {
                        RuhamaLogger.log("attempting to place +z");
                    }
                    this.render = blockpos4;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos8, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));


                    if (this.getSettings().get(0).toToggle().state) {
                        this.resetRotation();
                    }


            }
        }

        // -x bed

            if (this.mc.world.getBlockState(this.blockpos9).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    this.lookAtPacket(this.mc.player.posX + 2.0D, this.mc.player.posY + 1.0D, this.mc.player.posZ, this.mc.player);

                    if (this.getSettings().get(3).toToggle().state) {
                        RuhamaLogger.log("attempting to place -x");
                    }
                    this.render = blockpos5;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos9, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));


                    if (this.getSettings().get(0).toToggle().state) {
                        this.resetRotation();

                }
            }
        }

        // -z bed
            if (this.mc.world.getBlockState(this.blockpos10).getMaterial().isReplaceable()) {
                if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable()) {

                    this.lookAtPacket(this.mc.player.posX, this.mc.player.posY + 1.0D, this.mc.player.posZ + 2.0D, this.mc.player);

                    if (this.getSettings().get(3).toToggle().state) {
                        RuhamaLogger.log("attempting to place -x");
                    }
                    this.render = blockpos6;
                    this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockpos10, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));


                    if (this.getSettings().get(0).toToggle().state) {
                        this.resetRotation();
                    }
            }
        }
    }



    public void onEnable(){
        RuhamaLogger.log("AutoBedCity: ENABLED");
    }
    public void onDisable() {
        RuhamaLogger.log("AutoBedCity: DISABLED");
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

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me)
    {
        double[] v = this.calculateLookAt(px, py, pz, me);

        this.setYawAndPitch((float) v[0], (float) v[1]);
    }

    private void setYawAndPitch(float yaw1, float pitch1)
    {
        this.isSpoofingAngles = true;
    }

    public double[] calculateLookAt(double px, double py, double pz, EntityPlayer me)
    {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0D / 3.141592653589793D;
        yaw = yaw * 180.0D / 3.141592653589793D;
        yaw += 90.0D;

        return new double[] {yaw, pitch};
    }

    private void resetRotation()
    {
        if (this.isSpoofingAngles)
        {
            this.isSpoofingAngles = false;
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
