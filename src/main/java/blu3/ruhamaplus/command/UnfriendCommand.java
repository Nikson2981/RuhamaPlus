package blu3.ruhamaplus.command;

import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.ArrayList;
import java.util.List;

public class UnfriendCommand extends CommandBase implements IClientCommand {
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".unfriend";
    }

    private List<String> aliases = new ArrayList<>();

    public UnfriendCommand(){
        aliases.add(".uf");
    }


    public List<String> getAliases(){
        return aliases;
    }


    public String getUsage(ICommandSender sender) { return null; }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 0) {
            ClientChat.warn("/.unfriend [name]");
        }

        if(args.length == 1){
            FriendManager.Get().removeFriend(args[0]);
        }
    }
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
