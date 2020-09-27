package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.WorldUtils;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.block.BlockBed;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WurstPlusBedAura extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0.0D, 20.0D, 16.0D, 0, "Delay: "),
            new SettingSlider(0.0D, 8.0D, 5.0D, 0, "Range: "));

    public WurstPlusBedAura() {
        super("WurstPlusBedAura", 0, Category.COMBAT, "asdfghjsregihidfugbfdhgl", settings);
    }

    private BlockPos render_pos;
    private spoof_face spoof_looking;


    public void onEnable() {
        this.render_pos = null;
    }


    public void onDisable() {
        this.render_pos = null;
    }

    public void onRender() {

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

        if (this.render_pos == null){
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(render_pos), red, 0.7F, blue, 0.25F);
        }
    }

    public void onUpdate() {
        if (this.mc.player == null) return;

        if (this.mc.player.ticksExisted % this.getSetting(0).asSlider().getValue() == 0) {
            this.place_bed();
            this.break_bed();
            this.refill_bed();
        }
    }

    public void refill_bed() {
        if (!(this.mc.currentScreen instanceof GuiContainer)) {
            if (this.mc.player.inventory.getCurrentItem().getItem() == Items.AIR) {
                for (int i = 9; i < 35; ++i) {
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                        this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, this.mc.player);
                        break;
                    }
                }
            }
        }
    }

    public void break_bed() {
        
        int x;
        double range = this.getSetting(1).asSlider().getValue();
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

    public int find_bed() {
        for (int i = 0; i < 9; i++) {
            if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                return i;
            }
        }
        return -1;
    }

    public void place_bed() {

        if (find_bed() == -1) {
            //ClientChat.warn("CANNOT FIND BEDS");
            //this.setToggled(false);
            return;
        }

        int bed_slot = find_bed();

        BlockPos best_pos = null;
        EntityPlayer best_target = null;
        float best_distance = (float) this.getSetting(1).asSlider().getValue();

        for (EntityPlayer player : this.mc.world.playerEntities.stream().filter(entityPlayer -> !FriendManager.Get().isFriend(entityPlayer.getName().toLowerCase())).collect(Collectors.toList())) {

            if (player == this.mc.player) continue;

            if (best_distance < this.mc.player.getDistance(player)) continue;

            boolean face_place = true;

            BlockPos pos = get_pos_floor(player).down();
            BlockPos pos2 = check_side_block(pos);

            if (pos2 != null) {
                best_pos = pos2.up();
                best_target = player;
                best_distance = this.mc.player.getDistance(player);
                face_place = false;
            }

            if (face_place) {
                BlockPos upos = get_pos_floor(player);
                BlockPos upos2 = check_side_block(upos);

                if (upos2 != null) {
                    best_pos = upos2.up();
                    best_target = player;
                    best_distance = this.mc.player.getDistance(player);
                }
            }
        }

        if (best_target == null) {
            //ClientChat.log("cant find best player");
            //this.setToggled(false);
            return;
        }

        render_pos = best_pos;

        if (spoof_looking == spoof_face.NORTH) {
            this.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(180, 0, this.mc.player.onGround));
        } else if (spoof_looking == spoof_face.SOUTH) {

            this.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 0, this.mc.player.onGround));
        } else if (spoof_looking == spoof_face.WEST) {

            this.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(90, 0, this.mc.player.onGround));
        } else if (spoof_looking == spoof_face.EAST) {

            this.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(-90, 0, this.mc.player.onGround));
        }
        WorldUtils.placeBlock(best_pos, bed_slot, false, false);
    }

    public BlockPos check_side_block(BlockPos pos) {
        if (this.mc.world.getBlockState(pos.east()).getBlock() != Blocks.AIR && this.mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.WEST;
            return pos.east();
        }
        if (this.mc.world.getBlockState(pos.north()).getBlock() != Blocks.AIR && this.mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.SOUTH;
            return pos.north();
        }
        if (this.mc.world.getBlockState(pos.west()).getBlock() != Blocks.AIR && this.mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.EAST;
            return pos.west();
        }
        if (this.mc.world.getBlockState(pos.south()).getBlock() != Blocks.AIR && this.mc.world.getBlockState(pos.south().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.NORTH;
            return pos.south();
        }

        return null;

    }

    public static BlockPos get_pos_floor(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    enum spoof_face {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }
}
