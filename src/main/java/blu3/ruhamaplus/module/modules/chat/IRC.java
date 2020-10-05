package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.irc.IRCClient;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.ClientChat;

import java.io.IOException;

public class IRC extends Module {
    public IRC() { super("IRC", 0, Category.CHAT, "internal relay chat", null);
        this.ircClient = RuhamaPlus.getIRCClient();
    }

    private blu3.ruhamaplus.irc.IRCClient ircClient;


    public void onEnable(){
        if (ircClient.isActive())
            ClientChat.log("You are already connected to the IRC.");
        try {
            ircClient.start();
            ircClient.joinChannel(ircClient.getChannel());
        } catch (IOException e) {
            e.printStackTrace();
            ClientChat.log("Unable to connect to the IRC.");
        }
        ClientChat.log("Connecting to the IRC...");
    }

    public void onDisable() {
        if (!(ircClient.isActive()))
            ClientChat.log("You are not connected to the IRC.");

        try {
            ircClient.quit("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
            ClientChat.log("Unable to disconnect from the IRC.");
        }
        ClientChat.log("Succesfully disconnected from the IRC.");
    }
}
