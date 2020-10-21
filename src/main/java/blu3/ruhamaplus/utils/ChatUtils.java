package blu3.ruhamaplus.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils implements Util
{

    public static void loadWords(){
        System.out.println("Initializing badwords");
        for (String bruh : BadWords.getBadWords()){
            sialydf.add(bruh);
            System.out.println(bruh);
        }
    }
    public static void log(String text)
    {
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.AQUA + "[Ruhama+]: " + TextFormatting.RESET + text));
    }

    public static void warn(String text)
    {
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.YELLOW + "[Ruhama+: WARN]: " + TextFormatting.RESET + text));
    }

    public static void error(String location, String text)
    {
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.RED + "[Ruhama+: ERROR]: " + TextFormatting.RESET + "at " + TextFormatting.RED + location + ": " + TextFormatting.RESET + text));
    }

    public static void testError(Object location, String text){
        if (Minecraft.getMinecraft().world != null)
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(TextFormatting.RED + "[Ruhama+: ERROR]: " + TextFormatting.RESET + "at " + TextFormatting.RED + location + ": " + TextFormatting.RESET + text));
    }

    public static List<String> sialydf = new ArrayList<>();

    public static void cleanChatMessage(String message) {

        StringBuilder cleanMessage = new StringBuilder();

        String[] arr = message.split(" ", 256); // char limit for chat
        for (String bruh : arr) {
            if (sialydf.contains(bruh.toLowerCase())) {
                for (int i = 0; i < bruh.length(); i++){
                    cleanMessage.append("#");
                }
                cleanMessage.append(" ");
            } else {
                cleanMessage.append(bruh + " ");
            }
        }
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(cleanMessage.toString()));
    }
}
