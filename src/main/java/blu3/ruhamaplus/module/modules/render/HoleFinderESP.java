package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ReflectUtils;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;


import java.util.*;

public class HoleFinderESP extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingSlider(5.0D, 25.0D, 10.0D, 0, "Range: "), new SettingMode("Draw: ", "Full", "Flat"), new SettingToggle(true, "Rainbow"), new SettingSlider(0.0D, 255.0D, 255.0D, 0, "Obby-R: "), new SettingSlider(0.0D, 255.0D, 0.0D, 0, "Obby-G: "), new SettingSlider(0.0D, 255.0D, 0.0D, 0, "Obby-B: "), new SettingSlider(0.0D, 255.0D, 100.0D, 0, "Bedrk-R: "), new SettingSlider(0.0D, 255.0D, 100.0D, 0, "Bedrk-G: "), new SettingSlider(0.0D, 255.0D, 255.0D, 0, "Bedrk-B: "), new SettingSlider(0.0D, 100.0D, 20.0D, 0, "Tick Delay"));
    private final List<BlockPos> poses = new ArrayList<>();
    public Vec3d prevPos;
    private double[] rPos;

    public HoleFinderESP()
    {
        super("HoleESP", 0, Category.RENDER, "Finds Holes In Bedrock/Obsidian", settings);
        this.prevPos = Vec3d.ZERO;
    }

    public void onUpdate()
    {
        if (this.mc.player.ticksExisted % this.getSettings().get(9).asSlider().getValue() == 0 || (this.mc.player.getPositionVector().distanceTo(this.prevPos) > 5.0 && this.mc.player.ticksExisted % 10 == 0)) {
            this.update((int)this.getSettings().get(0).asSlider().getValue());
        }
    }

    public void update(int range)
    {
        this.poses.clear();
        final BlockPos player = this.mc.player.getPosition();
        this.prevPos = this.mc.player.getPositionVector();
        for (int y = -Math.min(range, player.getY()); y < Math.min(range, 255 - player.getY()); ++y) {
            for (int x = -range; x < range; ++x) {
                for (int z = -range; z < range; ++z) {
                    final BlockPos pos = player.add(x, y, z);
                    if ((this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || this.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) && this.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR && (this.mc.world.getBlockState(pos.up().east()).getBlock() == Blocks.BEDROCK || this.mc.world.getBlockState(pos.up().east()).getBlock() == Blocks.OBSIDIAN) && (this.mc.world.getBlockState(pos.up().west()).getBlock() == Blocks.BEDROCK || this.mc.world.getBlockState(pos.up().west()).getBlock() == Blocks.OBSIDIAN) && (this.mc.world.getBlockState(pos.up().north()).getBlock() == Blocks.BEDROCK || this.mc.world.getBlockState(pos.up().north()).getBlock() == Blocks.OBSIDIAN) && (this.mc.world.getBlockState(pos.up().south()).getBlock() == Blocks.BEDROCK || this.mc.world.getBlockState(pos.up().south()).getBlock() == Blocks.OBSIDIAN) && this.mc.world.getBlockState(pos.up(2)).getBlock() == Blocks.AIR && this.mc.world.getBlockState(pos.up(3)).getBlock() == Blocks.AIR) {
                        this.poses.add(pos.up());
                    }
                }
            }
        }
    }

    public void onRender() {
        try {
            this.rPos = new double[] { (double)ReflectUtils.getField(RenderManager.class, "renderPosX", "field_78725_b").get(this.mc.getRenderManager()), (double)ReflectUtils.getField(RenderManager.class, "renderPosY", "field_78726_c").get(this.mc.getRenderManager()), (double)ReflectUtils.getField(RenderManager.class, "renderPosZ", "field_78723_d").get(this.mc.getRenderManager()) };
        }
        catch (Exception e) {
            this.rPos = new double[] { 0.0, 0.0, 0.0 };
        }
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0f);
        float blue = System.currentTimeMillis() / 10L % 512L / 255.0f;
        float red = System.currentTimeMillis() / 16L % 512L / 255.0f;
        if (blue > 1.0f) {
            blue = 1.0f - blue;
        }
        if (red > 1.0f) {
            red = 1.0f - red;
        }
        for (final BlockPos p : this.poses) {
            this.drawFilledBlockBox(p, red, 0.7f, blue, 0.25f);
        }
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public void drawFilledBlockBox(final BlockPos blockPos, final float r, final float g, final float b, final float a) {
        try {
            final double x = blockPos.getX() - this.rPos[0];
            final double y = blockPos.getY() - this.rPos[1];
            final double z = blockPos.getZ() - this.rPos[2];
            final float or = (float)(this.getSettings().get(3).asSlider().getValue() / 255.0);
            final float og = (float)(this.getSettings().get(4).asSlider().getValue() / 255.0);
            final float ob = (float)(this.getSettings().get(5).asSlider().getValue() / 255.0);
            final float br = (float)(this.getSettings().get(6).asSlider().getValue() / 255.0);
            final float bg = (float)(this.getSettings().get(7).asSlider().getValue() / 255.0);
            final float bb = (float)(this.getSettings().get(8).asSlider().getValue() / 255.0);
            if (this.getSettings().get(2).asToggle().state) {
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), r, g, b, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), r, g, b, a * 1.5f);
            }
            else if (this.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.OBSIDIAN) {
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), or, og, ob, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), or, og, ob, a * 1.5f);
            }
            else {
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), br, bg, bb, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y, z + 1.0), br, bg, bb, a * 1.5f);
            }
            if (this.getSettings().get(1).asMode().mode == 1) {
                return;
            }
            if (this.getSettings().get(2).asToggle().state) {
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z), r, g, b, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z), r, g, b, a * 1.5f);
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x, y + 1.0, z + 1.0), r, g, b, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x, y + 1.0, z + 1.0), r, g, b, a * 1.5f);
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x + 1.0, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x + 1.0, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b, a * 1.5f);
                RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0), r, g, b, a);
                RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0), r, g, b, a * 1.5f);
            }
            else {
                if (this.mc.world.getBlockState(blockPos.north()).getBlock() == Blocks.OBSIDIAN) {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z), or, og, ob, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z), or, og, ob, a * 1.5f);
                }
                else {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z), br, bg, bb, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z), br, bg, bb, a * 1.5f);
                }
                if (this.mc.world.getBlockState(blockPos.west()).getBlock() == Blocks.OBSIDIAN) {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x, y + 1.0, z + 1.0), or, og, ob, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x, y + 1.0, z + 1.0), or, og, ob, a * 1.5f);
                }
                else {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z, x, y + 1.0, z + 1.0), br, bg, bb, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x, y + 1.0, z + 1.0), br, bg, bb, a * 1.5f);
                }
                if (this.mc.world.getBlockState(blockPos.east()).getBlock() == Blocks.OBSIDIAN) {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x + 1.0, y, z, x + 1.0, y + 1.0, z + 1.0), or, og, ob, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x + 1.0, y, z, x + 1.0, y + 1.0, z + 1.0), or, og, ob, a * 1.5f);
                }
                else {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x + 1.0, y, z, x + 1.0, y + 1.0, z + 1.0), br, bg, bb, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x + 1.0, y, z, x + 1.0, y + 1.0, z + 1.0), br, bg, bb, a * 1.5f);
                }
                if (this.mc.world.getBlockState(blockPos.south()).getBlock() == Blocks.OBSIDIAN) {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0), or, og, ob, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0), or, og, ob, a * 1.5f);
                }
                else {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(x, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0), br, bg, bb, a);
                    RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z + 1.0, x + 1.0, y + 1.0, z + 1.0), br, bg, bb, a * 1.5f);
                }
            }
        }
        catch (Exception ex) {}
    }
}
