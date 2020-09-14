package blu3.ruhamaplus.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ClientChat
{
    public static void log(String text)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.AQUA + "[Ruhama+]: " + TextFormatting.RESET + text));
    }

    public static void warn(String text)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.YELLOW + "[Ruhama+: WARN]: " + TextFormatting.RESET + text));
    }

    public static void error(String location, String text)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.RED + "[Ruhama+: ERROR]: " + TextFormatting.RESET + "at " + TextFormatting.RED + location + ": " + TextFormatting.RESET + text));
    }

    public static void testError(final Class location, String text){
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.RED + "[Ruhama+: ERROR]: " + TextFormatting.RESET + "at " + TextFormatting.RED + location + ": " + TextFormatting.RESET + text));
    }
}
