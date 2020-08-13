package blu3.FyreHack;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.FMLLog;

public class DiscordPresence {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private static final String APP_ID = "684623822263287991";
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    private static final String DEFAULT_DETAILS = "main menu";
    private static final String DEFAULT_STATE = "test";
    static String lastChat;
    private static DiscordRichPresence presence = new DiscordRichPresence();
    private static boolean hasStarted = false;
    private static String details;
    private static String state;
    private static ServerData svr;
    private static String[] popInfo;

    public static void start() {
        FMLLog.log.info("Starting Discord RPC");
        if (blu3.FyreHack.DiscordPresence.hasStarted) {
            return;
        }
        blu3.FyreHack.DiscordPresence.hasStarted = true;
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        blu3.FyreHack.DiscordPresence.rpc.Discord_Initialize(APP_ID, handlers, true, "");
        blu3.FyreHack.DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        blu3.FyreHack.DiscordPresence.presence.details = DEFAULT_DETAILS;
        blu3.FyreHack.DiscordPresence.presence.state = DEFAULT_STATE;
        blu3.FyreHack.DiscordPresence.presence.largeImageKey = "nutgod";
        blu3.FyreHack.DiscordPresence.rpc.Discord_UpdatePresence(blu3.FyreHack.DiscordPresence.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    blu3.FyreHack.DiscordPresence.rpc.Discord_RunCallbacks();
                    details = "bruh?";
                    state = "";
                    if (mc.isIntegratedServerRunning()) {
                        details = mc.player.getName();
                        state = "SinglePlayer";
                    } else if (mc.getCurrentServerData() != null) {
                        svr = mc.getCurrentServerData();
                        if (!svr.serverIP.equals("")) {
                            details = mc.player.getName();
                            state = svr.serverIP + " ";
                            if (svr.populationInfo != null) {
                                popInfo = svr.populationInfo.split("/");
                            }
                        }
                    } else {
                        details = DEFAULT_DETAILS;
                        state = DEFAULT_STATE;
                    }
                    if (!details.equals(blu3.FyreHack.DiscordPresence.presence.details) || !state.equals(blu3.FyreHack.DiscordPresence.presence.state)) {
                        blu3.FyreHack.DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    blu3.FyreHack.DiscordPresence.presence.details = details;
                    blu3.FyreHack.DiscordPresence.presence.state = state;
                    blu3.FyreHack.DiscordPresence.rpc.Discord_UpdatePresence(blu3.FyreHack.DiscordPresence.presence);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
        FMLLog.log.info("Discord RPC initialised succesfully");
    }
}
