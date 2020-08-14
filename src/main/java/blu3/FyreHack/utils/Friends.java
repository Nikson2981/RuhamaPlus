package blu3.FyreHack.utils;

import blu3.FyreHack.module.ModuleManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Friends {
    List<String> names = new ArrayList<>();
    public Friends() {
            try {
                URL pastebin = new URL("https://pastebin.com/raw/GekTqGDv");
                BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    names.add(inputLine);
                }
            } catch (Exception e) {
            }
        }

    public boolean isFriend(String name){
        return names.contains(name);
    }



    // how 2 friends:  if ((ModuleManager.getModuleByName("Friends").isToggled()) && !FyreHack.getInstance().friends.isFriend(username))
}