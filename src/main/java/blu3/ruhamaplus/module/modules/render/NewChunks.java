package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.RenderUtils;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.init.Biomes;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class NewChunks extends Module {
    public NewChunks() {
        super("NewChunks", 0, Category.CHAT, "briusdah ul \"green\"", null);
    }


    private List<ChunkData> chunkDataList = new ArrayList<>();
    private ICamera frustum = new Frustum();

    public boolean onPacketRead(Packet<?> packet) {
        if (packet instanceof SPacketChunkData) {
            final SPacketChunkData yoooooooooo = (SPacketChunkData) packet;
            if (!yoooooooooo.isFullChunk())
            {
                final ChunkData chunk = new ChunkData(yoooooooooo.getChunkX() * 16, yoooooooooo.getChunkZ() * 16);

                if (!this.chunkDataList.contains(chunk))
                {
                    this.chunkDataList.add(chunk);
                }
            }
        }
        return false;
    }


    public void onRender() {
        for (ChunkData chunkData : new ArrayList<ChunkData>(this.chunkDataList))
        {
            if (chunkData != null)
            {
                this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

                final AxisAlignedBB bb = new AxisAlignedBB(chunkData.x, 0, chunkData.z, chunkData.x + 16, 1, chunkData.z + 16);

                if (frustum.isBoundingBoxInFrustum(bb))
                {
                    //RenderUtil.drawPlane(chunkData.x - mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, chunkData.z - mc.getRenderManager().viewerPosZ, new AxisAlignedBB(0, 0, 0, 16, 1, 16), 1, 0xFF9900EE);
                    //RenderUtils.drawFilledBlockBox(new AxisAlignedBB(c.getBlock(0, 0, 0), c.getBlock(16, 0, 16)), 1.0F, 0.0F, 0.0F, 0.3F);

                }
            }
        }
    }

    public static class ChunkData
    {
        private int x;
        private int z;

        public ChunkData(int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        public int getX()
        {
            return x;
        }

        public void setX(int x)
        {
            this.x = x;
        }

        public int getZ()
        {
            return z;
        }

        public void setZ(int z)
        {
            this.z = z;
        }
    }
}

