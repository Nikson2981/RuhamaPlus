package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;

public class Annoyer extends Module {
    public Annoyer() {
        super("Annoyer", 0, Category.CHAT, " \u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+  " +
                "\u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+", null);
        this.delay = new Timer();
    }

    private final Timer delay;

    public boolean onPacketRead(Packet<?> packet)
    {
        if (this.delay.passedMs(4000)){
            if (!(this.mc.player == null) && !(this.mc.world == null)) {
                if (packet instanceof SPacketChat) {
                    SPacketChat sPacketChat = (SPacketChat) packet;
                    String message = sPacketChat.getChatComponent().getUnformattedText();

                    if (message.contains(" \u23D0 \u0280\u1d1c\u029c\u1d00\u1d0d\u1d00+") && !(message.contains(this.mc.player.getName()))){

                        String[] arr = message.split(" ", 2); // splits the message at " " into 2 parts

                        mc.player.sendChatMessage(arr[1]); // sends the second part


                        this.delay.reset();
                    }
                }
            }
        }
        return false;
    }
}