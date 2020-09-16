package blu3.ruhamaplus.module;

import blu3.ruhamaplus.module.modules.chat.*;
import blu3.ruhamaplus.module.modules.combat.*;
import blu3.ruhamaplus.module.modules.experimental.TestAutoTotem;
import blu3.ruhamaplus.module.modules.exploits.*;
import blu3.ruhamaplus.module.modules.gui.*;
import blu3.ruhamaplus.module.modules.misc.*;
import blu3.ruhamaplus.module.modules.render.*;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ModuleManager
{
    private static final List<Module> mods = Arrays.asList(
            new CrystalAura(),
            //new AutoBedCrafter(),
            new Capes(),
            new Annoyer(),
            new ChestSwap(),
            new PvpInfo(),
            new TabNames(),
            new Hud(),
            new BedObsidianTrap(),
            new OldBedAura(),
            new SwingArm(),
            new Aura(),
            new Closest(),
            new VisualRange(),
            new CoordHud(),
            new LongRangeAim(),
            //new AirBedPlace(),
            new BedAuraECME(),
            new AntiChunkBan(),
            new Auto32k(),
            new AutoLog(),
            new AutoTotem(),
            new AutoWither(),
            new ClickGui(),
            new TotemPopCounter(),
            new Crasher(),
            new DispenserAura(),
            new ElytraFly(),
            new ElytraReplace(),
            new FOVSlider(),
            //new GreenText(),
            new TestAutoTotem(),
            new Gui(),
            new HoleFiller(),
            new HoleFinderESP(),
            new HopperNuker(),
            new HopperRadius(),
            new InvSorter(),
            new NBTViewer(),
            new NewAuto32k(),
            new ObsidianTrap(),
            new PearlViewer(),
            new Peek(),
            new PlayerRadar(),
            new FeetXp(),
            new ChatSuffix(),
            new ShulkerAura(),
            new StashFinder(),
            new StrengthESP(),
            new Surround(),
            new ThunderHack(),
            new TreeAura(),
            new TunnelESP(),
            new FogColour());

   public static List<Module> getModules()
    {
        return new ArrayList<>(mods);
    }

    public static Module getModuleByName(String name)
    {
        Iterator<Module> modsIter = mods.iterator();
        Module m;

        do
        {
            if (!modsIter.hasNext())
            {
                return null;
            }

            m = modsIter.next();
        } while (!name.equalsIgnoreCase(m.getName()));

        return m;
    }

    public static List<Module> getModulesInCat(Category cat)
    {
        List<Module> ms = new ArrayList<>();

        for (Module m : mods)
        {
            if (m.getCategory().equals(cat))
            {
                ms.add(m);
            }
        }

        return ms;
    }

    public static void onUpdate()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.onUpdate();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void onRender()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.onRender();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void onOverlay()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled())
                {
                    m.onOverlay();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean onPacketRead(Packet<?> packet)
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled() && m.onPacketRead(packet))
                {
                    return true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean onPacketSend()
    {
        for (Module m : mods)
        {
            try
            {
                if (m.isToggled() && m.onPacketSend())
                {
                    return true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void updateKeys()
    {
        if (Minecraft.getMinecraft().currentScreen == null)
        {
            for (Module m : mods)
            {
                try
                {
                    if (Keyboard.isKeyDown(m.getKey().getKeyCode()) && !m.keyActive)
                    {
                        m.keyActive = true;
                        m.toggle();
                    } else if (!Keyboard.isKeyDown(m.getKey().getKeyCode()))
                    {
                        m.keyActive = false;
                    }
                } catch (Exception ignored)
                {
                }
            }
        }
    }
}
