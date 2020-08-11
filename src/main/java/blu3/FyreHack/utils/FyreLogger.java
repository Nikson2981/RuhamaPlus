package blu3.FyreHack.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class FyreLogger
{
    public static void log(String text)
    {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.AQUA + "[Fyre Hack]: " + TextFormatting.RESET + text));
    }
}
