package bleach.a32k.module.modules;

import bleach.a32k.module.Category;
import bleach.a32k.module.Module;
import bleach.a32k.settings.SettingBase;
import bleach.a32k.settings.SettingSlider;
import bleach.a32k.settings.SettingToggle;
import bleach.a32k.utils.WorldUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AutoBedCity extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "2b Bypass"),  new SettingSlider(0.0D, 20.0D, 20.0D, 0, "Tick Delay: "));

    BlockPos blockpos1;
    BlockPos blockpos2;
    BlockPos blockpos3;
    BlockPos blockpos4;
    BlockPos blockpos5;
    BlockPos blockpos6;
    BlockPos blockpos7;
    BlockPos blockpos8;
    BlockPos blockpos9;
    BlockPos blockpos10;

    private EntityPlayer target;

    public AutoBedCity() { super("AutoBedCity", 0, Category.COMBAT, "cities people with beds automatically", settings); }

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
        return stack != null && stack.getItem() == Item.getItemById(355);
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
            this.blockpos7 = new BlockPos(player.posX + 1.0D, player.posY + 1.0D, player.posZ); // +x bed
            this.blockpos8 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ + 1.0D); // +z bed
            this.blockpos9 = new BlockPos(player.posX - 1.0D, player.posY + 1.0D, player.posZ); // -x bed
            this.blockpos10 = new BlockPos(player.posX, player.posY + 1.0D, player.posZ - 1.0D); // -z bed

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

                        if (this.mc.world.getBlockState(this.blockpos8).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos8, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos9).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos9, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
                        }

                        if (this.mc.world.getBlockState(this.blockpos10).getMaterial().isReplaceable())
                        {
                            WorldUtils.placeBlock(this.blockpos10, this.mc.player.inventory.currentItem, this.getSettings().get(0).toToggle().state, false);
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
