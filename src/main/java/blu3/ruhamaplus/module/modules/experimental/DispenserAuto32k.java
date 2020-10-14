package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.BlockUtil;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.WorldUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DispenserAuto32k extends Module {
    public DispenserAuto32k() {
        super("DispenserAuto32k", 0, Category.EXPERIMENTAL, "Automatically dispenses a 32k.", settings);
    }

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0, 20, 10, 0, "Clicks/Tick"),
            new SettingSlider(0, 10, 6, 1, "Range"),
            new SettingToggle(false, "Fill Hopper"),
            new SettingToggle(false, "Drop Sword"),
            new SettingMode("Rotate: ", "Hard", "Soft")/*,
            new SettingToggle(true, "CPS Timeout"),
            new SettingSlider(0, 500, 400, 10, "Timeout After:")*/
    );

    private int stage;
    private int obsidian;
    private int dispenser;
    private int redstone;
    private int shulker;
    private int hopper;

    private BlockPos target;
    private BlockPos targetFront;
    private Block targetFrontBlock;
    private Block targetBlock;

    private boolean skippedStageOne = false;
    private boolean isAirPlacing = false;



    public void onEnable() {
        if (nullCheck()) {
            this.setToggled(false);
            return;
        }

        skippedStageOne = false;
        isAirPlacing = false;

        stage = 0;
        obsidian = -1;
        dispenser = -1;
        redstone = -1;
        shulker = -1;
        hopper = -1;

        target = null; //lets hope this doesnt fuck us later

        for (int i = 0; i < 9; i++) {

            if (obsidian != -1
                    && dispenser != -1
                    && redstone != -1
                    && shulker != -1
                    && hopper != -1) break;

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block == Blocks.OBSIDIAN) {
                obsidian = i;
            } else if (block == Blocks.DISPENSER) {
                dispenser = i;
            } else if (block == Blocks.REDSTONE_BLOCK) {
                redstone = i;
            } else if (block == Blocks.HOPPER) {
                hopper = i;
            } else if (BlockUtil.shulkers.contains(block)) {
                shulker = i;
            }

        }

        if (obsidian == -1) {
            ClientChat.log(TextFormatting.BLUE + "[Auto32k] " + TextFormatting.RESET + "Missing Obsidian!");
            this.setToggled(false);
            return;
        } else if (dispenser == -1) {
            ClientChat.log(TextFormatting.BLUE + "[Auto32k] " + TextFormatting.RESET + "Missing Dispenser!");
            this.setToggled(false);
            return;
        } else if (redstone == -1) {
            ClientChat.log(TextFormatting.BLUE + "[Auto32k] " + TextFormatting.RESET + "Missing Redstone Block!");
            this.setToggled(false);
            return;
        } else if (hopper == -1) {
            ClientChat.log(TextFormatting.BLUE + "[Auto32k] " + TextFormatting.RESET + "Missing Hopper!");
            this.setToggled(false);
            return;
        } else if (shulker == -1) {
            ClientChat.log(TextFormatting.BLUE + "[Auto32k] " + TextFormatting.RESET + "Missing Shulker Box!");
            this.setToggled(false);
            return;
        }

        if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.objectMouseOver.getBlockPos().up() == null) {
            ClientChat.log(TextFormatting.BLUE + "[Auto32k] " + TextFormatting.RESET + "Not a valid target!");
            this.setToggled(false);
            return;
        }

        target = mc.objectMouseOver.getBlockPos();
        targetFront = target.offset(mc.player.getHorizontalFacing().getOpposite());
        targetFrontBlock = mc.world.getBlockState(targetFront).getBlock();
        targetBlock = mc.world.getBlockState(target).getBlock();
    }

    public void onDisable() {
        if (nullCheck()) return;
    }


    public void fastUpdate(){
        if (stage == 6) //faster killaura rofl
        {
            this.killAura();
        }
    }

    public void onUpdate() {
        if (nullCheck()) {
            this.setToggled(false);
            return;
        }
        if (stage == 6)
        {
            if (!(mc.currentScreen instanceof GuiHopper) && this.getBoolean("Drop Sword")) {
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, mc.player.inventory.currentItem + 36, 0, ClickType.PICKUP, mc.player);
                this.mc.playerController.windowClick(mc.player.inventoryContainer.windowId, -999, 0, ClickType.PICKUP, mc.player);
                this.setToggled(false);
                return;
            }
        }

        if (stage == 0) {
            if(targetFrontBlock instanceof BlockAir && !(targetBlock instanceof BlockAir)) {
                target = target.add(0, -1, 0);
                skippedStageOne = true;
                stage = 1;
                return;
            }

            if(targetFrontBlock instanceof BlockAir && targetBlock instanceof BlockAir) {
                isAirPlacing = true;
                skippedStageOne = false;
            }

            mc.player.inventory.currentItem = obsidian;
            placeBlock(target.add(0, 1, 0), EnumFacing.DOWN);
            stage = 1;
            return;
        }

        if (stage == 1) {
            mc.player.inventory.currentItem = dispenser;
            placeBlock(target.add(0, 2, 0), EnumFacing.DOWN);
            if(!isAirPlacing) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(target.add(0, 2, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
            } else {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(target.add(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
            }
            stage = 2;
            return;
        }

        if (stage == 2) {

            if (!(mc.currentScreen instanceof GuiDispenser)) {
                return;
            }

            mc.playerController.windowClick(mc.player.openContainer.windowId, 4, shulker, ClickType.SWAP, mc.player);
            mc.player.closeScreen();
            stage = 3;
            return;
        }

        if (stage == 3) //wow, this was a lot of work just for placing a redstone block
        {
            BlockPos attempt0 = target.add(0, 3, 0);
            BlockPos attempt1 = target.add(0, 2, 0).offset(mc.player.getHorizontalFacing().rotateY());
            BlockPos attempt2 = target.add(0, 2, 0).offset(mc.player.getHorizontalFacing().rotateYCCW());
            BlockPos attempt3 = target.add(0, 2, 0).offset(mc.player.getHorizontalFacing());
            EnumFacing towardplayer = mc.player.getHorizontalFacing().getOpposite();
            EnumFacing rightside = mc.player.getHorizontalFacing().rotateY().getOpposite();
            EnumFacing leftside = mc.player.getHorizontalFacing().rotateYCCW().getOpposite();

            mc.player.inventory.currentItem = redstone;
            Material block1 = mc.world.getBlockState(attempt0).getMaterial();
            Material block2 = mc.world.getBlockState(attempt1).getMaterial();
            Material block3 = mc.world.getBlockState(attempt2).getMaterial();
            Material block4 = mc.world.getBlockState(attempt3).getMaterial();
            Material[] blockall = {block1, block2, block3, block4};
            if (block1.isReplaceable()) {
                placeBlock(attempt0, EnumFacing.DOWN);
                stage = 4;
            }

            if ((block2.isReplaceable()) && !(block1.isReplaceable())) {
                placeBlock(attempt1, rightside);
                stage = 4;
            }

            if ((block3.isReplaceable()) && !(block1.isReplaceable()) && !(block2.isReplaceable())) {
                placeBlock(attempt2, leftside);
                stage = 4;
            }

            if ((block4.isReplaceable()) && !(block1.isReplaceable()) && !(block2.isReplaceable()) && !(block3.isReplaceable())) {
                placeBlock(attempt3, towardplayer);
                stage = 4;
            }

            if (!(block1.isReplaceable()) && !(block2.isReplaceable()) && !(block3.isReplaceable()) && !(block4.isReplaceable())) {
                ClientChat.log("No viable redstone place targets!");
                this.setToggled(false);
                return;
            }

        }

        if (stage == 4) {
            mc.player.inventory.currentItem = hopper;
            if (skippedStageOne || isAirPlacing) {
                placeBlock(targetFront, mc.player.getHorizontalFacing());
                WorldUtils.openBlock(targetFront);
            } else {
                placeBlock(targetFront.add(0, 1, 0), mc.player.getHorizontalFacing());
                WorldUtils.openBlock(targetFront.add(0, 1, 0));
            }
            mc.player.inventory.currentItem = shulker;

            stage = 5;
            return;
        }

        if (stage == 5) {
            if (!(mc.currentScreen instanceof GuiHopper)) {
                return;
            }

            if (((GuiContainer) mc.currentScreen).inventorySlots.getSlot(1).getStack().isEmpty()) {
                if (this.getBoolean("Fill Hopper")) {
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 4, obsidian, ClickType.SWAP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 4, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 4, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 3, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 2, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 1, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 0, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.openContainer.windowId, 0, obsidian, ClickType.SWAP, mc.player);
                }
            }


            if (((GuiContainer) mc.currentScreen).inventorySlots.getSlot(0).getStack().isEmpty()) {
                return;
            }

            stage = 6;
            return;
        }

        if (stage == 6) //this stage will run every tick until the gui is closed
        {
            if (!(mc.currentScreen instanceof GuiHopper)) {
                this.setToggled(false);
                return;
            }

            boolean isHandEmpty = mc.player.inventory.getCurrentItem().isEmpty();

            if (isHandEmpty) {
                mc.playerController.windowClick(mc.player.openContainer.windowId, 0, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
            }
        }

    }

    private void placeBlock(BlockPos pos, EnumFacing face) {
        BlockPos adj = pos.offset(face);
        EnumFacing opposite = face.getOpposite();
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        Vec3d hitVec = new Vec3d(adj).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, adj, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        mc.player.swingArm(EnumHand.MAIN_HAND);
        rotate(pos.getX(), pos.getY(), pos.getZ());
    }

    public void killAura()
    {
        for (int i = 0; (double) i < (this.getSlider("Clicks/Tick")); ++i)
        {
            Entity target = null;

            try
            {
                List<Entity> players = new ArrayList<>(this.mc.world.loadedEntityList);

                for (Object o : new ArrayList<>(players))
                {
                    Entity e = (Entity) o;

                    if (!(e instanceof EntityLivingBase))
                    {
                        players.remove(e);
                    }

                    if (FriendManager.Get().isFriend(e.getName().toLowerCase())){
                        players.remove(e);
                    }

                    if (e instanceof EntityLivingBase && ((EntityLivingBase) e).getHealth() <= 0)
                    {
                        players.remove(e);
                    }

                }

                players.remove(this.mc.player);
                players.sort((a, b) -> Float.compare(a.getDistance(this.mc.player), b.getDistance(this.mc.player)));

                if (players.get(0).getDistance(this.mc.player) < 8.0F)
                {
                    target = players.get(0);
                }
            } catch (Exception ignored)
            {
            }

            if (target == null)
            {
                return;
            }

            rotate(target.posX, target.posY + 1.0D, target.posZ);

            if (target.getDistance(this.mc.player) > this.getSlider("Range"))
            {
                return;
            }


            if (!(this.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD)) return;


            this.mc.playerController.attackEntity(this.mc.player, target);
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    void rotate(double x, double y, double z){
        switch (this.getMode("Rotate: ")) {
            case 0: {
                WorldUtils.rotateClient(x, y, z);
            }
            case 1: {
                WorldUtils.rotatePacket(x, y, z);
            }
        }
    }
}