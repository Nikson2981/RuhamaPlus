package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Dropper32k extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "2b Bypass"), new SettingToggle(true, "Killaura"), new SettingSlider(0.0D, 20.0D, 20.0D, 0, "CPS: "), new SettingMode("CPS: ", "Clicks/Sec", "Clicks/Tick", "Tick Delay"), new SettingToggle(false, "Timeout"), new SettingMode("Place: ", "Auto", "Looking"));

    private BlockPos pos;
    private int hopper;
    private int redstone;
    private int shulker;

    private int[] rot;

    private boolean active;
    private boolean openedDispenser;

    private int dispenserTicks;
    private int ticksPassed;

    private int timer = 0;

    public Dropper32k()
    {
        super("Dropper32k", 0, Category.COMBAT, "Dropper Auto32k", settings);
    }

    public void onEnable()
    {
        this.ticksPassed = 0;
        this.hopper = -1;
        int dispenser = -1;
        int dropper = -1;
        this.redstone = -1;
        this.shulker = -1;
        int block = -1;
        this.active = false;
        this.openedDispenser = false;
        this.dispenserTicks = 0;
        this.timer = 0;

        int x;

        for (x = 0; x <= 8; ++x)
        {
            Item item = this.mc.player.inventory.getStackInSlot(x).getItem();

            if (item == Item.getItemFromBlock(Blocks.HOPPER))
            {
                this.hopper = x;
            } else if (item == Item.getItemFromBlock(Blocks.DROPPER))
            {
                dropper = x;
            } else if (item == Item.getItemFromBlock(Blocks.DISPENSER))
            {
                dispenser = x;
            } else if (item == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
            {
                this.redstone = x;
            } else if (item instanceof ItemShulkerBox)
            {
                this.shulker = x;
            } else if (item instanceof ItemBlock)
            {
                block = x;
            }
        }

        if (this.hopper == -1)
        {
            ClientChat.log("Missing Hopper");
        } else if (dispenser == -1)
        {
            ClientChat.log("Missing Dispenser");
        } else if (dropper == -1)
        {
            ClientChat.log("Missing Dropper");
        } else if (this.redstone == -1)
        {
            ClientChat.log("Missing Redstone Block");
        } else if (this.shulker == -1)
        {
            ClientChat.log("Missing Shulker");
        } else if (block == -1)
        {
            ClientChat.log("Missing Generic Block");
        }

        if (this.hopper != -1 && dispenser != -1 && this.redstone != -1 && this.shulker != -1 && block != -1 && dropper != -1)
        {
            if (this.getSetting(5).asMode().mode == 1)
            {
                RayTraceResult ray = this.mc.player.rayTrace(5.0D, this.mc.getRenderPartialTicks());

                this.pos = Objects.requireNonNull(ray).getBlockPos().up();

                double xPos = (double) this.pos.getX() - this.mc.player.posX;
                double zPos = (double) this.pos.getZ() - this.mc.player.posZ;

                this.rot = Math.abs(xPos) > Math.abs(zPos) ? (xPos > 0.0D ? new int[] {-1, 0} : new int[] {1, 0}) : (zPos > 0.0D ? new int[] {0, -1} : new int[] {0, 1});

                if (WorldUtils.canPlaceBlock(this.pos) && WorldUtils.isBlockEmpty(this.pos) && WorldUtils.isBlockEmpty(this.pos.add(this.rot[0], 0, this.rot[1])) && WorldUtils.isBlockEmpty(this.pos.add(0, 1, 0)) && WorldUtils.isBlockEmpty(this.pos.add(0, 2, 0)) && WorldUtils.isBlockEmpty(this.pos.add(this.rot[0], 1, this.rot[1])))
                {
                    boolean rotate = this.getSetting(0).asToggle().state;

                    WorldUtils.placeBlock(this.pos, block, rotate, false);
                    WorldUtils.rotatePacket((double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5D, this.pos.getY() + 1, (double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5D);
                    WorldUtils.placeBlock(this.pos.add(0, 1, 0), dropper, false, false);
                    

                    WorldUtils.placeBlock(this.pos.add(this.rot[0], 1, this.rot[1]), dispenser, this.getSetting(0).asToggle().state, false);
                } else
                {
                    ClientChat.log("Unable to place 32k");

                    this.setToggled(false);
                }
            } else
            {
                for (x = -3; x <= 3; ++x)
                {
                    for (int y = -1; y <= 1; ++y)
                    {
                        for (int z = -3; z <= 3; ++z)
                        {
                            this.rot = Math.abs(x) > Math.abs(z) ? (x > 0 ? new int[] {-1, 0} : new int[] {1, 0}) : (z > 0 ? new int[] {0, -1} : new int[] {0, 1});
                            this.pos = this.mc.player.getPosition().add(x, y, z);

                            if (this.mc.player.getPositionEyes(this.mc.getRenderPartialTicks()).distanceTo(this.mc.player.getPositionVector().add(x - this.rot[0] / 2, (double) y + 0.5D, z + this.rot[1] / 2)) <= 4.5D && this.mc.player.getPositionEyes(this.mc.getRenderPartialTicks()).distanceTo(this.mc.player.getPositionVector().add((double) x + 0.5D, (double) y + 2.5D, (double) z + 0.5D)) <= 4.5D && WorldUtils.canPlaceBlock(this.pos) && WorldUtils.isBlockEmpty(this.pos) && WorldUtils.isBlockEmpty(this.pos.add(this.rot[0], 0, this.rot[1])) && WorldUtils.isBlockEmpty(this.pos.add(0, 1, 0)) && WorldUtils.isBlockEmpty(this.pos.add(0, 2, 0)) && WorldUtils.isBlockEmpty(this.pos.add(this.rot[0], 1, this.rot[1])))
                            {
                                boolean rotate = this.getSetting(0).asToggle().state;

                                WorldUtils.placeBlock(this.pos, block, rotate, false);
                                WorldUtils.rotatePacket((double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5D, this.pos.getY() + 1, (double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5D);
                                WorldUtils.placeBlock(this.pos.add(0, 1, 0), dropper, false, false);

                                WorldUtils.rotatePacket((double) this.pos.add(-this.rot[0], 1, -this.rot[1]).add(-this.rot[0], 1, -this.rot[1]).getX() + 0.5D, this.pos.getY() + 1, (double) this.pos.add(-this.rot[0], 1, -this.rot[1]).getZ() + 0.5D);
                                WorldUtils.placeBlock(this.pos.add(this.rot[0], 1, this.rot[1]), dispenser, this.getSetting(0).asToggle().state, false);

                                return;
                            }
                        }
                    }
                }

                ClientChat.log("Unable to place 32k");
                this.setToggled(false);
            }
        } else
        {
            this.setToggled(false);
        }
    }

    public void onUpdate()
    {
        if ((!this.getSetting(4).asToggle().state || this.active || this.ticksPassed <= 25) && (!this.active || this.mc.currentScreen instanceof GuiHopper))
        {
            if (this.active && this.getSetting(1).asToggle().state && this.timer == 0)
            {
                this.killAura();
            }

            if (this.mc.currentScreen instanceof GuiDispenser)
            {
                this.openedDispenser = true;
            }

            if (this.mc.currentScreen instanceof GuiHopper)
            {
                GuiHopper gui = (GuiHopper) this.mc.currentScreen;

                int slot;
                for (slot = 32; slot <= 40; ++slot)
                {
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, gui.inventorySlots.getSlot(slot).getStack()) > 5)
                    {
                        this.mc.player.inventory.currentItem = slot - 32;
                        break;
                    }
                }

                this.active = true;

                if (this.getSetting(3).asMode().mode == 0)
                {
                    this.timer = (long) this.timer >= Math.round(20.0D / this.getSetting(2).asSlider().getValue()) ? 0 : this.timer + 1;
                } else if (this.getSetting(3).asMode().mode == 1)
                {
                    this.timer = 0;
                } else if (this.getSetting(3).asMode().mode == 2)
                {
                    this.timer = (double) this.timer >= this.getSetting(2).asSlider().getValue() ? 0 : this.timer + 1;
                }

                if (!(gui.inventorySlots.inventorySlots.get(0).getStack().getItem() instanceof ItemAir) && this.active)
                {
                    slot = this.mc.player.inventory.currentItem;
                    boolean pull = false;

                    for (int i = 40; i >= 32; --i)
                    {
                        if (gui.inventorySlots.getSlot(i).getStack().isEmpty())
                        {
                            slot = i;
                            pull = true;
                            break;
                        }
                    }

                    if (pull)
                    {
                        this.mc.playerController.windowClick(gui.inventorySlots.windowId, 0, 0, ClickType.PICKUP, this.mc.player);
                        this.mc.playerController.windowClick(gui.inventorySlots.windowId, slot, 0, ClickType.PICKUP, this.mc.player);
                    }
                }
            }

            if (this.ticksPassed == 0)
            {
                WorldUtils.openBlock(this.pos.add(0, 1, 0));
                this.openedDispenser = true;
            }

            if (this.openedDispenser && this.dispenserTicks == 0 && this.mc.currentScreen instanceof GuiContainer)
            {
                this.mc.playerController.windowClick(this.mc.player.openContainer.windowId, 36 + this.shulker, 0, ClickType.QUICK_MOVE, this.mc.player);
                ++this.dispenserTicks;
                ++this.ticksPassed;
                this.mc.player.closeScreen();
            }

            if (this.dispenserTicks == 1)
            {
               WorldUtils.placeBlock(this.pos.add(0, 2, 0), this.redstone, this.getSetting(0).asToggle().state, false);
            }

            if (!this.active && this.mc.world.getBlockState(this.pos.add(this.rot[0], 1, this.rot[1])).getBlock() instanceof BlockDispenser && !(this.mc.world.getBlockState(this.pos.add(0, 2, 0)).getMaterial().isReplaceable()))
            {
                WorldUtils.placeBlock(this.pos.add(this.rot[0], 2, this.rot[1]), redstone, this.getSetting(0).asToggle().state, false);

            }

            if (!this.active && this.mc.world.getBlockState(this.pos.add(this.rot[0], 1, this.rot[1]).add(this.rot[0], 0, this.rot[1])).getBlock() instanceof BlockShulkerBox)
            {
                WorldUtils.placeBlock(this.pos.add(this.rot[0], 0, this.rot[1]).add(this.rot[0], 0, this.rot[1]), this.hopper, this.getSetting(0).asToggle().state, false);
                WorldUtils.openBlock(this.pos.add(this.rot[0], 0, this.rot[1]).add(this.rot[0], 0, this.rot[1]));
            }

            if (!this.active && this.mc.world.getBlockState(this.pos.add(this.rot[1], 1, this.rot[1])).getBlock() instanceof BlockShulkerBox)
            {
                WorldUtils.placeBlock(this.pos.add(this.rot[1], 0, this.rot[1]), this.hopper, this.getSetting(0).asToggle().state, false);
                WorldUtils.openBlock(this.pos.add(this.rot[1], 0, this.rot[1]));
            }

            if (!this.active && this.mc.world.getBlockState(this.pos.add(-this.rot[1], 1, this.rot[1])).getBlock() instanceof BlockShulkerBox)
            {
                WorldUtils.placeBlock(this.pos.add(-this.rot[1], 0, this.rot[1]), this.hopper, this.getSetting(0).asToggle().state, false);
                WorldUtils.openBlock(this.pos.add(-this.rot[1], 0, this.rot[1]));
            }

            ++this.ticksPassed;
        } else
        {
            this.setToggled(false);
        }
    }

    public void killAura()
    {
        for (int i = 0; (double) i < (this.getSetting(3).asMode().mode == 1 ? this.getSetting(2).asSlider().getValue() : 1.0D); ++i)
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

            WorldUtils.rotatePacket(target.posX, target.posY + 1.0D, target.posZ);

            if (target.getDistance(this.mc.player) > 6.0F)
            {
                return;
            }
            

            this.mc.playerController.attackEntity(this.mc.player, target);
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
        }

    }
}