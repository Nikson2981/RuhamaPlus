package blu3.ruhamaplus.utils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;

public class RenderUtils
{
    private static Minecraft mc;

    public static double[] rPos() {
        try {
            return new double[] { (double)ReflectUtils.getField(RenderManager.class, "renderPosX", "field_78725_b").get(RenderUtils.mc.getRenderManager()), (double)ReflectUtils.getField(RenderManager.class, "renderPosY", "field_78726_c").get(RenderUtils.mc.getRenderManager()), (double)ReflectUtils.getField(RenderManager.class, "renderPosZ", "field_78723_d").get(RenderUtils.mc.getRenderManager()) };
        }
        catch (Exception e) {
            return new double[] { 0.0, 0.0, 0.0 };
        }
    }

    public static void drawFilledBlockBox(AxisAlignedBB box, final float r, final float g, final float b, final float a) {
        try {
            glSetup();
            final double[] rPos = rPos();
            box = new AxisAlignedBB(box.minX - rPos[0], box.minY - rPos[1], box.minZ - rPos[2], box.maxX - rPos[0], box.maxY - rPos[1], box.maxZ - rPos[2]);
            RenderGlobal.renderFilledBox(box, r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5f);
            glCleanup();
        }
        catch (Exception ex) {}
    }


    public static void drawBlockBox(AxisAlignedBB box, final float r, final float g, final float b, final float a) {
        try {
            glSetup();
            final double[] rPos = rPos();
            box = new AxisAlignedBB(box.minX - rPos[0], box.minY - rPos[1], box.minZ - rPos[2], box.maxX - rPos[0], box.maxY - rPos[1], box.maxZ - rPos[2]);
            RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5f);
            glCleanup();
        }
        catch (Exception ex) {}
    }

    public static void drawText(final BlockPos pos, final String text) {

        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (EntityPlayer)mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        mc.fontRenderer.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        GlStateManager.popMatrix();
    }

    public static void glBillboard(final float x, final float y, final float z) {
        final float scale = 0.02666667f;
        try {
            GlStateManager.translate(x - (double) ReflectUtils.getField(RenderManager.class, "renderPosX", "field_78725_b").get(RenderUtils.mc.getRenderManager()), y - (double)ReflectUtils.getField(RenderManager.class, "renderPosY", "field_78726_c").get(RenderUtils.mc.getRenderManager()), z - (double)ReflectUtils.getField(RenderManager.class, "renderPosZ", "field_78723_d").get(RenderUtils.mc.getRenderManager()));
        } catch (Exception e) {}
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.player.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        glBillboard(x, y, z);
        final int distance = (int)player.getDistance(x, y, z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glSetup() {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0f);
    }

    public static void glCleanup() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}
