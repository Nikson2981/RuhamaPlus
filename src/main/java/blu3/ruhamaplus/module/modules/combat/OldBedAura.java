package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.RenderUtils;
import blu3.ruhamaplus.utils.Timer;
import blu3.ruhamaplus.utils.WorldUtils;
import net.minecraft.block.BlockBed;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.*;
import java.util.stream.Collectors;

public class OldBedAura extends Module
{

    private final Timer placeDelay;
    private final Timer breakDelay;

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingToggle(true, "AutoSwitch"), // 0
            new SettingToggle(true, "Players"), // 1
            new SettingToggle(false, "Mobs"), // 2
            new SettingToggle(false, "Animals"), // 3
            new SettingToggle(true, "Place"), // 4
            new SettingToggle(true, "Explode"), // 5
            new SettingToggle(true, "Chat Alert"), // 6
            new SettingToggle(false, "Fill Hotbar"), // 7
            new SettingSlider(0.0D, 6.0D, 4.25D, 0, "Range: "), // 8
            new SettingSlider(0.0D, 20.0D, 18.0D, 0, "PlaceDelay: "), // 9
            new SettingSlider(0.0D, 20.0D, 4.0D, 0, "BreakDelay: "), // 10
            new SettingMode("RotateMode: ", "toTarget", "Smart"), // 11
            new SettingToggle(false, "test 1.13+")); // 12


    private final List<BlockPos> placeTargets = new ArrayList<>();

    private BlockPos render;
    private BlockPos targetPos;


    private BlockPos plusX;
    private BlockPos plusXUp;

    private BlockPos plusZ;
    private BlockPos plusZUp;

    private BlockPos negX;
    private BlockPos negXUp;

    private BlockPos negZ;
    private BlockPos negZUp;

    private boolean togglePitch = false;
    private boolean switchCooldown = false;
    private boolean isAttacking = false;

    private boolean isSpoofingAngles;

    public OldBedAura()
    {
        super("1.12 BedAura", 0, Category.COMBAT, "enderperl guy want bedaur ok", settings);
        this.placeDelay = new Timer();
        this.breakDelay = new Timer();
    }


    public void onUpdate()
    {
        this.placeTargets.clear();

        if (this.breakDelay.passedMs((long) this.getSetting(10).asSlider().getValue() * 25L)) {
            if (this.getSetting(5).asToggle().state) {
                this.clickBed();
            }
         this.breakDelay.reset();
        }

        if (this.placeDelay.passedMs((long) this.getSetting(9).asSlider().getValue() * 25L)) {
            this.placeDelay.reset();
            this.moveBed();


            int bedSlot;

            bedSlot = this.mc.player.getHeldItemMainhand().getItem() == Items.BED ? this.mc.player.inventory.currentItem : -1;

            if (bedSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (this.mc.player.inventory.getStackInSlot(l).getItem() == Items.BED) {
                        bedSlot = l;
                        break;
                    }
                }
            }

            boolean offhand = false;

            if (this.mc.player.getHeldItemOffhand().getItem() == Items.BED) {
                offhand = true;
            } else if (bedSlot == -1) {
                return;
            }

            List<BlockPos> blocks = this.findBedBlocks();
            List<Entity> entities = new ArrayList<>();

            if (this.getSetting(1).asToggle().state) {
                entities.addAll(this.mc.world.playerEntities);
            }

            entities.addAll(this.mc.world.loadedEntityList.stream().filter((entityx) -> entityx instanceof EntityAnimal ? this.getSetting(3).asToggle().state : this.getSetting(2).asToggle().state).collect(Collectors.toList()));
            BlockPos q = null;

            double damage = 0.5D;

            Iterator<Entity> entityIter = entities.iterator();

            bruhFernflower:

            while (true) {
                Entity entity;
                do {
                    do {
                        if (!entityIter.hasNext()) {
                            if (damage == 0.5D) {
                                this.render = null;


                                return;
                            }

                            this.render = q.up();

                            if (this.getSetting(4).asToggle().state) {
                                if (!offhand && this.mc.player.inventory.currentItem != bedSlot) {
                                    if (this.getSetting(0).asToggle().state) {
                                        this.mc.player.inventory.currentItem = bedSlot;



                                        this.switchCooldown = true;
                                    }

                                    return;
                                }

                                this.lookAtPacket((double) q.getX() + 0.5D, (double) q.getY() - 0.5D, (double) q.getZ() + 0.5D, this.mc.player);



                               if (this.getSetting(11).asMode().mode == 0) { WorldUtils.rotateBedPacket(q, this.targetPos); }

                               if (this.getSetting(11).asMode().mode == 1){


                                   this.plusX = new BlockPos(q.getX() + 1.0D, q.getY(), q.getZ());
                                   this.plusXUp = new BlockPos(q.getX() + 1.0D, q.getY() + 1.0D, q.getZ());

                                   this.plusZ = new BlockPos(q.getX(), q.getY(), q.getZ() + 1.0D);
                                   this.plusZUp = new BlockPos(q.getX(), q.getY() + 1.0D, q.getZ() + 1.0D);

                                   this.negX = new BlockPos(q.getX() - 1.0D, q.getY(), q.getZ());
                                   this.negXUp = new BlockPos(q.getX() - 1.0D, q.getY() + 1.0D, q.getZ());

                                   this.negZ = new BlockPos(q.getX(), q.getY(), q.getZ() - 1.0D);
                                   this.negZUp = new BlockPos(q.getX(), q.getY() + 1.0D, q.getZ() - 1.0D);

                                       if (!(this.mc.world.getBlockState(this.plusX).getMaterial().isReplaceable())) {
                                           if (this.mc.world.getBlockState(this.plusXUp).getMaterial().isReplaceable()) {
                                               WorldUtils.rotateBedPacket(q, q.add(1,0,0));
                                           }
                                       }

                                       if (!(this.mc.world.getBlockState(this.plusZ).getMaterial().isReplaceable())) {
                                           if (this.mc.world.getBlockState(this.plusZUp).getMaterial().isReplaceable()) {
                                               WorldUtils.rotateBedPacket(q, q.add(0,0,1));
                                           }
                                       }

                                       if (!(this.mc.world.getBlockState(this.negX).getMaterial().isReplaceable())) {
                                           if (this.mc.world.getBlockState(this.negXUp).getMaterial().isReplaceable()) {
                                               WorldUtils.rotateBedPacket(q, q.add(-1,0,0));
                                           }
                                   }

                                       if (!(this.mc.world.getBlockState(this.negZ).getMaterial().isReplaceable())) {
                                           if (this.mc.world.getBlockState(this.negZUp).getMaterial().isReplaceable()) {
                                               WorldUtils.rotateBedPacket(q, q.add(0,0,-1));
                                       }
                                   }
                               }


                                this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                            }

                            if (this.isSpoofingAngles) {
                                EntityPlayerSP player;

                                if (this.togglePitch) {
                                    player = this.mc.player;
                                    player.rotationPitch = (float) ((double) player.rotationPitch + 4.0E-4D);
                                    this.togglePitch = false;
                                } else {
                                    player = this.mc.player;
                                    player.rotationPitch = (float) ((double) player.rotationPitch - 4.0E-4D);
                                    this.togglePitch = true;
                                }
                            }

                            return;
                        }

                        entity = (Entity) entityIter.next();
                    } while (entity == this.mc.player);
                } while (((EntityLivingBase) entity).getHealth() <= 0.0F);

                Iterator<BlockPos> blocksIter = blocks.iterator();

                while (true) {
                    BlockPos blockPos;

                    double d;
                    double self;

                    do {
                        do {
                            double dd;
                            do {
                                if (!blocksIter.hasNext()) {
                                    continue bruhFernflower;
                                }

                                blockPos = (BlockPos) blocksIter.next();
                                dd = entity.getDistanceSq(blockPos);
                            } while (dd >= 169.0D);

                            d = this.calculateDamage((double) blockPos.getX() + 0.5D, blockPos.getY() + 1, (double) blockPos.getZ() + 0.5D, entity);
                        } while (d <= damage);

                        self = this.calculateDamage((double) blockPos.getX() + 0.5D, blockPos.getY() + 1, (double) blockPos.getZ() + 0.5D, this.mc.player);
                    } while (self > d && d >= (double) ((EntityLivingBase) entity).getHealth());

                    this.targetPos = new BlockPos(entity.posX, entity.posY + 2.0D, entity.posZ);

                    if (self - 0.5D <= (double) this.mc.player.getHealth()) {
                        damage = d;
                        q = blockPos;
                    }
                }
            }
        }
    }

    public void clickBed() {

        int x;

        if (!(mc.player.dimension == 0)) {

            double range = this.getSetting(8).asSlider().getValue();
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
    }


    public void moveBed(){
        if (this.getSetting(7).asToggle().state) {

            if (mc.currentScreen instanceof GuiContainer) return;

            if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBed) return;

            for (int i = 0; i <= 9; i++)
                if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                    return;
                }

            int slot = -1;

            for (int i = 0; i <= 9; i++)
                if (mc.player.inventory.getStackInSlot(i).isEmpty() && slot == -1) {
                    slot = i;
                    break;
                }

            if (slot != -1 && this.mc.player.inventory.getItemStack().isEmpty()) {
                int t = -1;
                for (int i = 0; i <= 46; i++)
                    if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.BED && i > 9) {
                        t = i;
                        break;
                    }

                if (t != 1) {
                    if (mc.player.inventory.getStackInSlot(slot).isEmpty()) {
                        mc.playerController.windowClick(0, t, 0, ClickType.QUICK_MOVE, mc.player);
                    }
                }
            }
        }
    }


    public void onRender()
    {
        if (this.render != null)
        {
            //RenderUtils.drawFilledBlockBox(new AxisAlignedBB(this.render), 1.0F, 1.0F, 1.0F, 0.3F);
        }
        for (BlockPos p : this.placeTargets)
        {
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(p), 1.0F, 1.0F, 1.0F, 0.3F);
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me)
    {
        double[] v = this.calculateLookAt(px, py, pz, me);

        this.setYawAndPitch((float) v[0], (float) v[1]);
    }

    public double[] calculateLookAt(double px, double py, double pz, EntityPlayer me)
    {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0D / 3.141592653589793D;
        yaw = yaw * 180.0D / 3.141592653589793D;
        yaw += 90.0D;

        return new double[] {yaw, pitch};
    }

    private boolean canPlaceBed(BlockPos blockPos)
    {
        BlockPos boost = blockPos.add(0, 1, 0);

        if (this.getSetting(12).asToggle().state){
        return (this.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.NETHERRACK
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.NETHER_BRICK
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.DIRT
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.COBBLESTONE
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.STONE
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN
                || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR)
                && this.mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                && this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();

        } else return (this.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK
        || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.NETHERRACK
        || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.NETHER_BRICK
        || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.DIRT
        || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.COBBLESTONE
        || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.STONE
        || this.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
        && this.mc.world.getBlockState(boost).getBlock() == Blocks.AIR
        && this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
    }

    public BlockPos getPlayerPos()
    {
        return new BlockPos(Math.floor(this.mc.player.posX), Math.floor(this.mc.player.posY), Math.floor(this.mc.player.posZ));
    }

    private List<BlockPos> findBedBlocks()
    {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(this.getSphere(this.getPlayerPos(), (float) this.getSetting(8).asSlider().getValue(), (int) this.getSetting(8).asSlider().getValue(), false, true, 0).stream().filter(this::canPlaceBed).collect(Collectors.toList()));

        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y)
    {
        List<BlockPos> bedBlocks = new ArrayList<>();

        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x)
        {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z)
            {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y)
                {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F))))
                    {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        bedBlocks.add(l);
                    }
                }
            }
        }

        return bedBlocks;
    }

    public float calculateDamage(double posX, double posY, double posZ, Entity entity)
    {
        float doubleExplosionSize = 8.6F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;

        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;

        float damage = (float) ((int) ((v * v + v) / 2.0D * 9.0D * (double) doubleExplosionSize + 1.0D));
        double finald = 1.0D;

        if (entity instanceof EntityLivingBase)
        {
            finald = this.getBlastReduction((EntityLivingBase) entity, this.getDamageMultiplied(damage), new Explosion(this.mc.world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finald;
    }

    public float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);

            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp((float) k, 0.0F, 20.0F);

            damage *= 1.0F - f / 25.0F;

            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11))))
            {
                damage -= damage;
            }

            damage = Math.max(damage - ep.getAbsorptionAmount(), 0.0F);

            return damage;
        } else
        {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            return damage;
        }
    }

    private float getDamageMultiplied(float damage)
    {
        int diff = this.mc.world.getDifficulty().getId();

        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    private void setYawAndPitch(float yaw1, float pitch1)
    {
        this.isSpoofingAngles = true;
    }

    private void resetRotation()
    {
        if (this.isSpoofingAngles)
        {
            this.isSpoofingAngles = false;
        }
    }

    public void onEnable()
    {
        if (this.getSetting(6).asToggle().state)
        {
            ClientChat.log("1.12BedAura: ON");
        }
    }

    public void onDisable()
    {
        if (this.getSetting(6).asToggle().state)
        {
            ClientChat.log("1.12BedAura: OFF");
        }

        this.render = null;
        this.resetRotation();
    }
}
