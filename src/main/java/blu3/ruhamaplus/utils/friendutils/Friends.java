package blu3.ruhamaplus.utils.friendutils;

import blu3.ruhamaplus.RuhamaPlus;
import blu3.ruhamaplus.errors.BlacklistedHwidError;
import blu3.ruhamaplus.errors.InvalidHwidError;
import blu3.ruhamaplus.errors.NetworkError;
import blu3.ruhamaplus.settings.SettingMode;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import me.nrubin29.pastebinapi.*;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Friends {

    static String webhookUrl = "https://discordapp.com/api/webhooks/671768851205062676/R701bYpaurong--Jn5JC3N8LhN_HMg-HsBPwHkKmveXAhT32tuzMhO9j93Xnr0qcndIU";

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
                    //sendMessage(hwid + " tried to run again after being blacklisted! uh oh!", avatarLink);
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

                    /*try {
                        sendMessage(hwid + " ran the client succesfully.", avatarLink);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    return true;
                }
            }
            //sendMessage(hwid + " is an unknown HWID. Was this intentional?", avatarLink);
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
        } catch (Exception e) {
        }
    }
    private static String avatarLink = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/dd/dd313d2374823c676579f2d0ddf3632013857559_full.jpg";

    public static void sendMessage(String content, String avatarUrl){
        TemmieWebhook tm = new TemmieWebhook(avatarUrl);
        DiscordMessage dm = new DiscordMessage("Ruhama+ " + RuhamaPlus.version , content, avatarUrl);
        tm.sendMessage(dm);
    }
}