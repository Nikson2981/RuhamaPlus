package blu3.FyreHack.module.modules.fyrehack;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingMode;
import blu3.FyreHack.settings.SettingToggle;
import blu3.FyreHack.utils.WorldUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelfTrap extends Module
{
    public SelfTrap()
    {
        super("SelfTrap", 0, Category.COMBAT, "Build obsidian around you to protect you from crystals", Arrays.asList(new SettingMode("Mode: ", "1x1", "2x2", "Smart"), new SettingToggle(true, "Switch Back")));
    }

    public void onUpdate()
    {
        int obsidian = -1;

        int cap;

        for (cap = 0; cap < 9; ++cap)
        {
            if (this.mc.player.inventory.getStackInSlot(cap).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN))
            {
                obsidian = cap;

                break;
            }
        }

        cap = 0;
        List<BlockPos> poses = new ArrayList<>();

        boolean rotate = this.getSettings().get(3).toToggle().state;

        if (this.getSettings().get(0).toMode().mode == 0)
        {
            poses.addAll(Arrays.asList((new BlockPos(this.mc.player.getPositionVector())).add(0, 0, 1), (new BlockPos(this.mc.player.getPositionVector())).add(1, 0, 0), (new BlockPos(this.mc.player.getPositionVector())).add(0, 0, -1), (new BlockPos(this.mc.player.getPositionVector())).add(-1, 0, 0), (new BlockPos(this.mc.player.getPositionVector())).add(0, 1, 0)));
        } else if (this.getSettings().get(0).toMode().mode == 1)
        {
            poses.addAll(Arrays.asList((new BlockPos(this.mc.player.getPositionVector())).add(0, 0, 2), (new BlockPos(this.mc.player.getPositionVector())).add(2, 0, 0), (new BlockPos(this.mc.player.getPositionVector())).add(0, 0, -2), (new BlockPos(this.mc.player.getPositionVector())).add(-2, 0, 0)));
        } else if (this.getSettings().get(0).toMode().mode == 2)
        {
            poses.addAll(Arrays.asList((new BlockPos(this.mc.player.getPositionVector().add(0.0D, 0.0D, -this.mc.player.width))).add(0, 0, -1), (new BlockPos(this.mc.player.getPositionVector().add(-this.mc.player.width, 0.0D, 0.0D))).add(-1, 0, 0), (new BlockPos(this.mc.player.getPositionVector().add(0.0D, 0.0D, this.mc.player.width))).add(0, 0, 1), (new BlockPos(this.mc.player.getPositionVector().add(this.mc.player.width, 0.0D, 0.0D))).add(1, 0, 0)));
        }

        for (Object o : new ArrayList<>(poses))
        {
            BlockPos b = (BlockPos) o;
            poses.add(0, b.down());

            poses.add(0, b.up());
        }


        if (obsidian != -1)
        {
            int hand = this.mc.player.inventory.currentItem;

            for (BlockPos b : poses)
            {
                if (WorldUtils.placeBlock(b, obsidian, rotate, false))
                {
                    ++cap;
                }

                if (cap > 2)
                {
                    break;
                }
            }

            if (this.getSettings().get(1).toToggle().state)
            {
                this.mc.player.inventory.currentItem = hand;
            }
        }
    }
}
