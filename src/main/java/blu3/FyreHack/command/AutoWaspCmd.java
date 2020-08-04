package blu3.FyreHack.command;

import blu3.FyreHack.module.ModuleManager;
import blu3.FyreHack.module.modules.fyrehack.AutoWasp;
import blu3.FyreHack.utils.FyreLogger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

import java.util.Objects;

public class AutoWaspCmd extends CommandBase implements IClientCommand {
    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) { return false; }

    @Override
    public String getName() { return "autowasp"; }

    @Override
    public String getUsage(ICommandSender sender) { return null; }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1)
        {
            FyreLogger.log("Invalid usage, use /autowasp [name]");
        } else {
            try
            {
                ((AutoWasp) (ModuleManager.getModuleByName("WaspAimbot"))).resetTarget();
                ((AutoWasp) Objects.requireNonNull(ModuleManager.getModuleByName("WaspAimbot"))).setTarget(args[0]);
            } catch (Exception e)
            {
                FyreLogger.log("how did you fucking do it wrong");
            }
        }
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
