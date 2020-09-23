package blu3.ruhamaplus.utils.friendutils;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.BlacklistedHwidError;
import blu3.ruhamaplus.settings.InvalidHwidError;
import blu3.ruhamaplus.settings.NetworkError;
import blu3.ruhamaplus.settings.SettingMode;
import me.nrubin29.pastebinapi.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Friends {

    public static boolean tryValidateHwid() {
        System.out.println("trying to validate hwid");
        final String hwid = SettingMode.getHwid();
        List<String> valids = new ArrayList<>();
        List<String> invalids = new ArrayList<>();

        try {

            URL blacklist = new URL("https://pastebin.com/raw/R3fENbYr");
            BufferedReader lines = new BufferedReader(new InputStreamReader(blacklist.openStream()));
            String inputLine2;
            while ((inputLine2 = lines.readLine()) != null) {
                invalids.add(inputLine2);
                if (invalids.contains(hwid)) {
                    System.out.println(hwid + " is a blacklisted HWID.");
                    postInvalid(hwid);
                    throw new BlacklistedHwidError(hwid);
                }
            }

            URL pastebin = new URL("https://pastebin.com/raw/qjKmBpbL");
            BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                valids.add(inputLine);
                if (valids.contains(hwid + " " + RuhamaPlus.version)) {
                    System.out.println(hwid + " is a valid HWID, and you are on the correct version.");
                    return true;
                }
            }


            postUnknown(hwid);
            throw new InvalidHwidError(hwid);
        }
        catch (Exception e) {
            System.out.println("couldnt connect to sevrer ok");
            throw new NetworkError();
        }
    }

    public static void postUnknown(String hwid) throws IOException, PastebinException {
        System.out.println(hwid + " is not valid.");
        PastebinAPI api = new PastebinAPI("aqW4JeaC1jbJq5QjgPvgH4jD8YSpVPjm");
        User user = api.getUser("HWIDBruh", "blu3#0895lol"); // alt
        String name = "Name : " + Minecraft.getMinecraft().getSession().getUsername();
        CreatePaste paste = user.createPaste()
                .withName(name + " HWID")
                .withFormat(Format.None)
                .withPrivacyLevel(PrivacyLevel.UNLISTED)
                .withExpireDate(ExpireDate.ONE_WEEK)
                .withText(hwid + " " + RuhamaPlus.version);
        paste.post();
    }

    public static void postInvalid(String hwid) throws IOException, PastebinException {
        try {
            URL bruh = new URL("http://bot.whatismyipaddress.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(bruh.openStream()));
            String da = "dah: " + br.readLine().trim();
            br.close();

            PastebinAPI api = new PastebinAPI("aqW4JeaC1jbJq5QjgPvgH4jD8YSpVPjm");
            User user = api.getUser("HWIDBruh", "blu3#0895lol"); // alt
            String name = "Name : " + Minecraft.getMinecraft().getSession().getUsername();
            CreatePaste paste = user.createPaste()
                    .withName(name + " HWID (blacklisted)")
                    .withFormat(Format.None)
                    .withPrivacyLevel(PrivacyLevel.UNLISTED)
                    .withExpireDate(ExpireDate.ONE_WEEK)
                    .withText(hwid + " " + RuhamaPlus.version + " " + da);
            paste.post();
        } catch (Exception e) {}
    }


    /*
    public static boolean blacklisted(){
        final String hwid = SettingMode.getHwid();
        List<String> invalids = new ArrayList<>();
        try {
            URL pastebin = new URL("https://pastebin.com/raw/R3fENbYra");
            BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                invalids.add(inputLine);
                if (invalids.contains(hwid)) {
                    System.out.println(hwid + " is a blacklisted HWID.");
                    return true;
                }
            }
            return false;
        }
        catch (Exception e) {
            System.out.println("couldnt connect to sevrer ok");
            throw new NetworkError();
        }
        }
     */

}