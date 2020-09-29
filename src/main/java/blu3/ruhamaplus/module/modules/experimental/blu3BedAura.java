package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.*;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class blu3BedAura extends Module {

    private boolean switchCooldown = false;
    private BlockPos renderTarget = null;
    private BlockPos playerPos = null;
    private final List<EntityPlayer> ezplayers = new ArrayList<>();
    private final List<EntityPlayer> targetPlayers = new ArrayList<>();

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0.0D, 6.0D, 5.0D, 0, "Range: "), //0
            new SettingSlider(0.0D, 20.0D, 3.0D, 0, "Place Delay: "), //1
            new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MinDMG: "), //2
            new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MaxSelfDMG: "), //3
            new SettingToggle(true, "AutoSwitch") //4

    );

    public blu3BedAura() { super("blu3BA", 0, Category.EXPERIMENTAL, "absolute shit i say part 2", settings); }

    public void onDisable(){
        ClientChat.log("blu3BA: " + ChatFormatting.RED + "OFF");
        ezplayers.clear();
        targetPlayers.clear();
        renderTarget = null;
    }
    public void onEnable(){
        ClientChat.log("blu3BA: " + ChatFormatting.AQUA + "ON");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.mc.player.ticksExisted % this.getSetting(1).asSlider().getValue() == 0) {
            this.placeBed();
            this.breakBed();
        }

        if (this.mc.getDebugFPS() < 5) {
            this.setToggled(false);
            ClientChat.warn("FPS dropped below 5, disbling for safety");
        }
    }

    private void placeBed(){
        ezplayers.clear();
        int bedSlot;
        bedSlot = this.mc.player.getHeldItemMainhand().getItem() == Items.BED ? this.mc.player.inventory.currentItem : -1;
        if (bedSlot == -1)
        {
            for (int l = 0; l < 9; ++l)
            {
                if (this.mc.player.inventory.getStackInSlot(l).getItem() == Items.BED)
                {
                    bedSlot = l;
                    break;
                }
            }
        }
        if (bedSlot == -1) { return; }
        int minDmg;
        minDmg = (int)this.getSetting(2).asSlider().getValue();

        List<BlockPos> blocks = this.findCrystalBlocks();
        ezplayers.addAll(this.mc.world.playerEntities);
        ezplayers.remove(this.mc.player);
        for (Object o : new ArrayList<>(ezplayers)) {
            Entity e = (EntityPlayer) o;
            if (FriendManager.Get().isFriend(e.getName().toLowerCase())){
                ezplayers.remove(e);
            }
        }

        //if (blocks.isEmpty()) return;
        //if (ezplayers.isEmpty()) return;
        BlockPos placeTarget = null;
        float highDamage = 0.5f;
        for (BlockPos pos : blocks) {
            final float selfDmg = DamageUtil.calculateDamage(pos, this.mc.player);
            if (selfDmg + 0.5D >= this.mc.player.getHealth() || selfDmg > this.getSetting(3).asSlider().getValue()) {
                continue;
            }
            for (EntityPlayer player : ezplayers) {
                targetPlayers.remove(player);
                if (player.getDistanceSq(pos) < square(this.getSetting(0).asSlider().getValue())){
                    if (!targetPlayers.contains(player)) targetPlayers.add(player);
                    float damage = DamageUtil.calculateDamage(pos, player);
                    if (damage <= selfDmg) continue;
                    if (damage <= minDmg || damage <= highDamage) continue;
                    highDamage = damage;
                    placeTarget = pos;
                    renderTarget = pos.up();
                    playerPos = new BlockPos(player.posX, player.posY, player.posZ);
                }
            }
        }
        if (placeTarget != null){
            if (this.getSetting(4).asToggle().state && this.mc.player.inventory.currentItem != bedSlot && !eatingGap()) {
                this.mc.player.inventory.currentItem = bedSlot;
                this.switchCooldown = true;
                return;
            }
            if (this.switchCooldown)
            {
                this.switchCooldown = false;
                return;
            }
            if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBed) {
                WorldUtils.smartBedRotation(placeTarget);
                this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placeTarget.up(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
            }
        }
    }

    public void breakBed(){

    }

    public void onRender() {
        if (this.renderTarget != null){
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(renderTarget), 0, 1, 1, 0.3F);
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(WorldUtils.getNextSmartPos(renderTarget.down())), 0, 1, 1, 0.3F);
        }
    }

    private List<BlockPos> findCrystalBlocks()
    {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(this.getSphere(this.getPlayerPos(), (float) this.getSetting(0).asSlider().getValue(), (int) this.getSetting(0).asSlider().getValue(), false, true, 0).stream().filter(this::canPlaceBed).collect(Collectors.toList()));

        return positions;
    }
    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y)
    {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x)
        {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z)
            {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y)
                {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F))))
                    {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    private boolean eatingGap(){
        return mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && mc.player.isHandActive();
    }
    public static double square(final double input) {
        return input * input;
    }
    public BlockPos getPlayerPos()
    {
        return new BlockPos(Math.floor(this.mc.player.posX), Math.floor(this.mc.player.posY), Math.floor(this.mc.player.posZ));
    }
    private boolean canPlaceBed(BlockPos blockPos){
        BlockPos boost = blockPos.add(0, 1, 0);
        return (this.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && WorldUtils.getNextSmartPos(boost.down()) != null);
    }
}