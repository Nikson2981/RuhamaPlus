package blu3.ruhamaplus.command;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.exploits.FakePlayer;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakePlayerCmd extends CommandBase implements IClientCommand {

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }

    public String getName()
    {
        return ".fakeplayer";
    }

    public String getUsage(ICommandSender sender) { return null; }

    private List<String> aliases = new ArrayList<>();

    public FakePlayerCmd(){
        aliases.add(".fp");
    }

    public List<String> getAliases(){
        return aliases;
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        (((FakePlayer) Objects.requireNonNull(ModuleManager.getModuleByName("FakePlayer")))).name = args[1];
    }
}
