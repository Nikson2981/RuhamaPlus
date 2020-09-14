package blu3.ruhamaplus.utils;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.settings.InvalidHwidError;
import blu3.ruhamaplus.settings.NetworkError;
import blu3.ruhamaplus.settings.SettingMode;
import me.nrubin29.pastebinapi.*;
import net.minecraft.client.Minecraft;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Friends {

    public static boolean tryValidateHwid() throws PastebinException, IOException {
        System.out.println("trying to validate hwid");
        final String hwid = SettingMode.getHwid();
        List<String> valids = new ArrayList<>();
        try {
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
            String url = paste.post();
            System.out.println("hwid sent to " + url);
            throw new InvalidHwidError(hwid);
        }
        catch (Exception e) {
            System.out.println("couldnt connect to sevrer ok");
            throw new NetworkError();
        }
    }
}