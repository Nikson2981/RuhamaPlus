package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Aura extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(true, "WeaponFilter"), new SettingToggle(true, "1.9 Delay"), new SettingToggle(true, "Thru Walls"), new SettingToggle(true, "Crits"), new SettingSlider(0.0D, 6.0D, 4.5D, 2, "Range: "), new SettingSlider(0.0D, 20.0D, 8.0D, 0, "CPS: "));
    private int delay = 0;

    public Aura()
    {
        super("Aura", 0, Category.COMBAT, "Attacks Players", settings);
    }



    public void onUpdate()
    {
        ++this.delay;
        final int reqDelay = (int)Math.round(20.0 / this.getSettings().get(5).asSlider().getValue());
        if (this.getSettings().get(0).asToggle().state && !(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe)) {
            return;
        }
        for (final EntityPlayer e : this.mc.world.playerEntities) {
            if (this.mc.player.getDistance((Entity)e) <= this.getSettings().get(4).asSlider().getValue() && e.getHealth() > 0.0f && e != this.mc.player && e != this.mc.player.getRidingEntity() && e != this.mc.getRenderViewEntity()) {
                if (!this.mc.player.canEntityBeSeen((Entity)e) && !this.getSettings().get(2).asToggle().state) {
                    continue;
                }
                if (FriendManager.Get().isFriend(e.getName().toLowerCase())){
                    continue;
                }
                if (((this.delay <= reqDelay && reqDelay != 0) || this.getSettings().get(1).asToggle().state) && (this.mc.player.getCooledAttackStrength(this.mc.getRenderPartialTicks()) < 1.0f || !this.getSettings().get(1).asToggle().state)) {
                    continue;
                }
                if (this.getSettings().get(3).asToggle().state) {
                    final Random rng = new Random();
                    final double n = 1.282622531E-314;
                    final double n2 = 1.282622531E-314 + 1.282622531E-314 * (1.0 + rng.nextInt(rng.nextBoolean() ? 34 : 43));
                    final double[] array2;
                    final double[] array = array2 = new double[] { 1.531232163E-314 + n2, 0.0, 1.135895857E-315 + n2, 0.0 };
                    for (final double d : array2) {
                        this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + d, this.mc.player.posZ, false));
                    }
                }
                this.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)e, EnumHand.MAIN_HAND));
                this.mc.playerController.attackEntity((EntityPlayer)this.mc.player, (Entity)e);
                this.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.delay = 0;
            }
        }
    }
    public void onEnable(){ this.delay = 0; }

}
