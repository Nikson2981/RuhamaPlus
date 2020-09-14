package blu3.ruhamaplus;

import blu3.ruhamaplus.command.*;
import blu3.ruhamaplus.gui.AdvancedText;
import blu3.ruhamaplus.gui.NewRuhamaGui;
import blu3.ruhamaplus.gui.TextWindow;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.gui.ClickGui;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.utils.*;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import blu3.ruhamaplus.utils.Friends;
import me.nrubin29.pastebinapi.PastebinException;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;

@Mod(
        modid = "ruhamaplus",
        name = "Ruhama+",
        version = "0.9",
        acceptedMinecraftVersions = "[1.12.2]"
)
public class RuhamaPlus
{
    public static Minecraft mc = Minecraft.getMinecraft();
    public static HashMap<BlockPos, Integer> friendBlocks = new HashMap<>();

    //public static final me.zero.alpine.EventBus EVENT_BUS = new EventManager();

    private long timer = 0L;
    private boolean timerStart = false;

    public Friends lockUtils;
    public CapeUtils capeUtils;

    private static FriendManager m_FriendManager = new FriendManager();

    public static String version = "0.9";

    public static FriendManager GetFriendManager()
    {
        return m_FriendManager;
    }

@Mod.Instance private static RuhamaPlus INSTANCE;

public RuhamaPlus() { INSTANCE = this;}

    public static RuhamaPlus getInstance(){
        return INSTANCE;
    }


    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException, PastebinException {

        Friends.tryValidateHwid();

        //ModuleManager.getModules().stream().forEach(EVENT_BUS::subscribe);



        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Rainbow());

        ClickGui.clickGui.initWindows();

        FileMang.init();

        FileMang.createFile("FriendList.json");

        FileMang.readModules();
        FileMang.readSettings();
        FileMang.readClickGui();
        FileMang.readBinds();
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
        ClientCommandHandler.instance.registerCommand(new LoginCmd());
        ClientCommandHandler.instance.registerCommand(new SetYawCmd());
        ClientCommandHandler.instance.registerCommand(new WaspCmd());
        ClientCommandHandler.instance.registerCommand(new HelpCmd());
        ClientCommandHandler.instance.registerCommand(new SaveConfigCmd());
        ClientCommandHandler.instance.registerCommand(new FriendCommand());
        ClientCommandHandler.instance.registerCommand(new UnfriendCommand());
        ClientCommandHandler.instance.registerCommand(new ToggleCmd());
        MinecraftForge.EVENT_BUS.register(new PeekCmd());
        lockUtils = new Friends();
        capeUtils = new CapeUtils();
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
            if (!(mc.currentScreen instanceof NewRuhamaGui))
            {
                Iterator textIter = NewRuhamaGui.textWins.iterator();

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
    public void chatSuffix(ClientChatEvent event)
    {
        if (Objects.requireNonNull(ModuleManager.getModuleByName("ChatSuffix") ).isToggled() && !event.getMessage().contains("\u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+") && !event.getMessage().startsWith("/") && !event.getMessage().startsWith("!"))
        {
            event.setCanceled(true);

            mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            mc.player.sendChatMessage(event.getMessage() + " \u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+");
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