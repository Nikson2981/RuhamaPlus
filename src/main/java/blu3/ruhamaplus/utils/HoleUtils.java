package blu3.ruhamaplus.utils;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HoleUtils implements Util{

    public boolean isBlockBlastResistant(BlockPos pos) {
        return (this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || this.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN);
    }
    public boolean isObsidian(BlockPos pos){
        return (this.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN);
    }

    public boolean isBedrock(BlockPos pos){
        return (this.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK);
    }
    public boolean isHole(BlockPos pos) {
        return (isBlockBlastResistant(pos.east()) && isBlockBlastResistant(pos.west()) && isBlockBlastResistant(pos.north()) && isBlockBlastResistant(pos.south()) && isBlockBlastResistant(pos.down()));
    }

    public boolean isGreenHole(BlockPos pos){
        return (isBedrock(pos.east()) && isBedrock(pos.west()) && isBedrock(pos.north()) && isBedrock(pos.south()) && isBedrock(pos.down()));
    }
    public boolean isYellowHole(BlockPos pos){
        List<BlockPos> spots = new ArrayList<>();
        spots.add(pos.east());
        spots.add(pos.west());
        spots.add(pos.north());
        spots.add(pos.south());
        spots.add(pos.down());
        int bedrocks = 0;
        int obbies = 0;

        for (BlockPos b : spots){
            if (isBedrock(b)) bedrocks++;
            if (isObsidian(b)) obbies++;
        }
        return (obbies >= 1 && bedrocks <= 4);
    }
    public boolean isRedHole(BlockPos pos){
        return (isObsidian(pos.east()) && isObsidian(pos.west()) && isObsidian(pos.north()) && isObsidian(pos.south()) && isObsidian(pos.down()));
    }
}
