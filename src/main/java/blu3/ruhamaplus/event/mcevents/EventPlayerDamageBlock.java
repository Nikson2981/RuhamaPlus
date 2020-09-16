package blu3.ruhamaplus.event.mcevents;

import blu3.ruhamaplus.event.MinecraftEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventPlayerDamageBlock extends MinecraftEvent
{
    private net.minecraft.util.math.BlockPos BlockPos;
    private EnumFacing Direction;

    public EventPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
    {
        BlockPos = posBlock;
        setDirection(directionFacing);
    }

    public BlockPos getPos()
    {
        return BlockPos;
    }

    /**
     * @return the direction
     */
    public EnumFacing getDirection()
    {
        return Direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(EnumFacing direction)
    {
        Direction = direction;
    }

}

