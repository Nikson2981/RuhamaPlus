package blu3.FyreHack;

import blu3.FyreHack.command.*;
import blu3.FyreHack.gui.AdvancedText;
import blu3.FyreHack.gui.NewFyreGui;
import blu3.FyreHack.gui.TextWindow;
import blu3.FyreHack.module.Module;
import blu3.FyreHack.module.ModuleManager;
import blu3.FyreHack.module.modules.render.ClickGui;
import blu3.FyreHack.settings.SettingBase;
import blu3.FyreHack.settings.SettingMode;
import blu3.FyreHack.settings.SettingSlider;
import blu3.FyreHack.utils.FileMang;
import blu3.FyreHack.utils.Friends;
import blu3.FyreHack.utils.Rainbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;

@Mod(
        modid = "fyrehack",
        name = "Fyre Hack",
        version = "0.2",
        acceptedMinecraftVersions = "[1.12.2]"
)
public class FyreHack
{
    public static Minecraft mc = Minecraft.getMinecraft();
    public static HashMap<BlockPos, Integer> friendBlocks = new HashMap<>();

    private long timer = 0L;
    private boolean timerStart = false;
    public Friends friends;


@Mod.Instance private static FyreHack INSTANCE;

public FyreHack() { INSTANCE = this;}

    public static FyreHack getInstance(){
        return INSTANCE;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DiscordPresence.start();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Rainbow());

        ClickGui.clickGui.initWindows();

        FileMang.init();

        FileMang.readModules();
        FileMang.readSettings();
        FileMang.readClickGui();
        FileMang.readBinds();
        FileMang.createFile("friends.txt");
        FileMang.createFile("chatlogger.txt");
        FileMang.readFriends();

        for (Module m : ModuleManager.getModules())
        {
            for (SettingBase s : m.getSettings())
            {
                if (s instanceof SettingMode)
                {
                    s.toMode().mode = MathHelper.clamp(s.toMode().mode, 0, s.toMode().modes.length - 1);
                } else if (s instanceof SettingSlider)
                {
                    s.toSlider().value = MathHelper.clamp(s.toSlider().value, s.toSlider().min, s.toSlider().max);
                }
            }
        }

        this.timerStart = true;
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new PeekCmd.PeekCommand());
        ClientCommandHandler.instance.registerCommand(new InvSorterCmd());
        ClientCommandHandler.instance.registerCommand(new StashFinderCmd());
        ClientCommandHandler.instance.registerCommand(new EntityDesyncCmd());
        MinecraftForge.EVENT_BUS.register(new PeekCmd());
        friends = new Friends();
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event)
    {
        if (mc.player != null && mc.world != null)
        {
            if (mc.world.isBlockLoaded(mc.player.getPosition()))
            {
                ModuleManager.onRender();
            }
        }
    }

    @SubscribeEvent
    public void onText(Text event)
    {
        if (event.getType().equals(ElementType.TEXT))
        {
            if (!(mc.currentScreen instanceof NewFyreGui))
            {
                Iterator textIter = NewFyreGui.textWins.iterator();

                label41:

                while (true)
                {
                    MutableTriple e;
                    do
                    {
                        if (!textIter.hasNext())
                        {
                            break label41;
                        }

                        e = (MutableTriple) textIter.next();
                    } while (!Objects.requireNonNull(ModuleManager.getModuleByName(((Module) e.left).getName())).isToggled());

                    int h = 2;

                    for (Iterator iter = ((TextWindow) e.right).getText().iterator(); iter.hasNext(); h += 10)
                    {
                        AdvancedText s = (AdvancedText) iter.next();
                        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());

                        int x = (double) ((TextWindow) e.right).posX > (double) scale.getScaledWidth() / 1.5D ? ((TextWindow) e.right).posX + ((TextWindow) e.right).len - mc.fontRenderer.getStringWidth(s.text) - 2 : (((TextWindow) e.right).posX < scale.getScaledWidth() / 3 ? ((TextWindow) e.right).posX + 2 : ((TextWindow) e.right).posX + ((TextWindow) e.right).len / 2 - mc.fontRenderer.getStringWidth(s.text) / 2);

                        if (s.shadow)
                        {
                            mc.fontRenderer.drawStringWithShadow(s.text, (float) x, (float) (((TextWindow) e.right).posY + h), s.color);
                        } else
                        {
                            mc.fontRenderer.drawString(s.text, x, ((TextWindow) e.right).posY + h, s.color);
                        }
                    }
                }
            }

            ModuleManager.onOverlay();
        }
    }

    @SubscribeEvent
    public void fyreSuffix(ClientChatEvent event)
    {
        if (Objects.requireNonNull(ModuleManager.getModuleByName("ChatSuffix") ).isToggled() && !event.getMessage().contains("Fyre Hack") && !event.getMessage().startsWith("/"))
        {
            event.setCanceled(true);

            mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            mc.player.sendChatMessage(event.getMessage() + " \u23D0 \u0493\u028f\u0280\u1d07 \u029c\u1d00\u1d04\u1d0b");
        }
    }


    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (System.currentTimeMillis() - 5000L > this.timer && this.timerStart)
        {
            this.timer = System.currentTimeMillis();

            FileMang.saveClickGui();
            FileMang.saveSettings();
            FileMang.saveModules();
            FileMang.saveBinds();
        }

        if (event.phase == Phase.START && mc.player != null && mc.world != null)
        {
            if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, 0.0D, mc.player.posZ)))
            {
                ModuleManager.onUpdate();
                ModuleManager.updateKeys();

                Entry e;

                try
                {
                    for (Iterator iter = friendBlocks.entrySet().iterator(); iter.hasNext(); friendBlocks.replace((BlockPos) e.getKey(), (Integer) e.getValue() - 1))
                    {
                        e = (Entry) iter.next();
                        if ((Integer) e.getValue() <= 0)
                        {
                            friendBlocks.remove(e.getKey());
                        }
                    }
                } catch (Exception ignored)
                {
                }
            }
        }
    }
}
