package blu3.ruhamaplus.command;

import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.FileMang;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

/**
 ** @author blu3
 **/

public class SaveConfigCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".saveconfig";
    }

    public String getUsage(ICommandSender sender)
    {
        return null;
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        FileMang.saveClickGui();
        FileMang.saveSettings();
        FileMang.saveModules();
        FileMang.saveBinds();
        FileMang.saveFriends(); //this should never be necessary\

        FileMang.readModules();
        FileMang.readSettings();
        FileMang.readClickGui();
        FileMang.readBinds();
        FileMang.readFriends(); //yeah again

        ClientChat.log("Saved Configurations.");
    }
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
