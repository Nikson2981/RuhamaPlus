package blu3.ruhamaplus.module.modules.combat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AutoLog extends Module
{
    private static final List<SettingBase> settings = Arrays.asList(new SettingToggle(false, "Health"), new SettingSlider(0.0D, 20.0D, 5.0D, 0, "Health: "), new SettingToggle(true, "Totems"), new SettingSlider(0.0D, 6.0D, 0.0D, 0, "Totems: "), new SettingMode("Crystal: ", "None", "Near", "Near+No Totem", "Near+Health"), new SettingSlider(0.0D, 8.0D, 4.0D, 2, "CrystalRange: "), new SettingToggle(false, "Nearby Player"), new SettingSlider(0.0D, 100.0D, 20.0D, 1, "Range: "));

    public AutoLog()
    {
        super("AutoLog", 0, Category.COMBAT, "Automatically Logs out when ___", settings);
    }

    public void onUpdate()
    {
        if (!this.mc.player.capabilities.isCreativeMode)
        {
            if (this.getSettings().get(0).asToggle().state && (double) this.mc.player.getHealth() < this.getSettings().get(1).asSlider().getValue())
            {
                this.logOut("Logged Out At " + this.mc.player.getHealth() + " Health");
            } else
            {
                if (this.getSettings().get(2).asToggle().state)
                {
                    int t = this.getTotems();

                    if (t <= (int) this.getSettings().get(3).asSlider().getValue())
                    {
                        this.logOut("Logged Out With " + t + " Totems Left");

                        return;
                    }
                }

                Iterator entityIter;

                if (this.getSettings().get(4).asMode().mode != 0)
                {
                    entityIter = this.mc.world.loadedEntityList.iterator();

                    while (entityIter.hasNext())
                    {
                        Entity e = (Entity) entityIter.next();

                        if (e instanceof EntityEnderCrystal)
                        {
                            double d = this.mc.player.getDistance(e);

                            if (d <= this.getSettings().get(5).asSlider().getValue() && (this.getSettings().get(4).asMode().mode == 1 || this.getSettings().get(4).asMode().mode == 2 && this.getTotems() <= (int) this.getSettings().get(3).asSlider().getValue() || this.getSettings().get(4).asMode().mode == 3 && (double) this.mc.player.getHealth() < this.getSettings().get(1).asSlider().getValue()))
                            {
                                this.logOut("Logged Out " + d + " Blocks Away From A Crystal");

                                return;
                            }
                        }
                    }
                }

                if (this.getSettings().get(6).asToggle().state)
                {
                    entityIter = this.mc.world.playerEntities.iterator();

                    while (entityIter.hasNext())
                    {
                        EntityPlayer e = (EntityPlayer) entityIter.next();

                        if (!e.getName().equals(this.mc.player.getName()) && (double) this.mc.player.getDistance(e) <= this.getSettings().get(7).asSlider().getValue())
                        {
                            this.logOut("Logged Out " + this.mc.player.getDistance(e) + " Blocks Away From A Player (" + e.getName() + ")");
                        }
                    }
                }

            }
        }
    }

    private int getTotems()
    {
        int c = 0;

        for (int i = 0; i < 45; ++i)
        {
            if (this.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING)
            {
                ++c;
            }
        }

        return c;
    }

    private void logOut(String reason)
    {
        this.mc.player.connection.getNetworkManager().closeChannel(new TextComponentString(reason));
        this.setToggled(false);
    }
}
