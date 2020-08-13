package blu3.FyreHack.module.modules.fyrehack;

import blu3.FyreHack.module.Category;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingToggle;
import blu3.FyreHack.utils.WorldUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BedObsidianTrap extends Module
{
    private static final List<SettingBase> settings = Collections.singletonList(new SettingToggle(false, "2b Bypass"));

    BlockPos blockpos1;
    BlockPos blockpos2;
    BlockPos blockpos3;
    BlockPos blockpos4;
    BlockPos blockpos5;
    BlockPos blockpos6;
    BlockPos blockpos7;

    private EntityPlayer target;

    public BedObsidianTrap()
    {
        super("BedObsidianTrap", 0, Category.COMBAT, "Boxes players in a obsidian box to bedcity", settings);
    }

    public boolean isInBlockRange(Entity target)
    {
        return target.getDistance(this.mc.player) <= 4.0F;
    }

    public boolean isValid(EntityPlayer entity)
    {
        return entity != null && this.isInBlockRange(entity) && entity.getHealth() > 0.0F && !entity.isDead;
    }

    private boolean isStackObby(ItemStack stack)
    {
        return stack != null && stack.getItem() == Item.getItemById(49);
    }

    private boolean doesHotbarHaveObby()
    {
        for (int i = 36; i < 45; ++i)
        {
            ItemStack stack = this.mc.player.inventoryContainer.getSlot(i).getStack();

            if (this.isStackObby(stack))
            {
                return true;
            }
        }

        return false;
    }


    public void onUpdate()
    {
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
                    if (this.isValid(this.target) && this.mc.player.getDistance(this.target) < 4.0F)
                    {
                        this.trap(this.target);
                    }

                    return;
                }

                player = playerIter.next();
            } while (player instanceof EntityPlayerSP || !this.isValid(player) || player.getDistance(this.mc.player) >= this.target.getDistance(this.mc.player));

            this.target = player;
        }
    }

    private void trap(EntityPlayer player)
    {
        if (this.doesHotbarHaveObby())
        {
            this.blockpos1 = new BlockPos(player.posX, player.posY + 2.0D, player.posZ);
            this.blockpos2 = new BlockPos(player.posX + 1.0D, player.posY + 2.0D, player.posZ);
            this.blockpos3 = new BlockPos(player.posX + 1.0D, player.posY, player.posZ);
            this.blockpos4 = new BlockPos(player.posX, player.posY, player.posZ + 1.0D);
            this.blockpos5 = new BlockPos(player.posX - 1.0D, player.posY, player.posZ);
            this.blockpos6 = new BlockPos(player.posX, player.posY, player.posZ - 1.0D);
            this.blockpos7 = new BlockPos(player.posX + 1.0D, player.posY + 1.0D, player.posZ);


            for (int i = 36; i < 45; ++i)
            {
                ItemStack stack = this.mc.player.inventoryContainer.getSlot(i).getStack();

                if (this.isStackObby(stack))
                {
                    int oldSlot = this.mc.player.inventory.currentItem;

                    if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable() || this.mc.world.getBlockState(this.blockpos3).getMaterial().isReplaceable() || this.mc.world.getBlockState(this.blockpos4).getMaterial().isReplaceable() || this.mc.world.getBlockState(this.blockpos5).getMaterial().isReplaceable() || this.mc.world.getBlockState(this.blockpos6).getMaterial().isReplaceable())
                    {
                        this.mc.player.inventory.currentItem = i - 36;

                        if (this.mc.world.getBlockState(this.blockpos3).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos3, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos4).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos4, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos5).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos5, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos6).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos6, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos7).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos7, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }


                        if (this.mc.world.getBlockState(this.blockpos2).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos2, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos1).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos1, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, this.getSettings().get(0).toToggle().state);
                        }

                        this.mc.player.inventory.currentItem = oldSlot;
                        break;
                    }
                }
            }
        }
    }

    public void onDisable()
    {
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
}
