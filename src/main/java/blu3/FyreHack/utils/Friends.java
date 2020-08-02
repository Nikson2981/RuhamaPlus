package blu3.FyreHack.utils;

import blu3.FyreHack.settings.Setting;
import blu3.FyreHack.settings.SettingBase;

import java.util.ArrayList;
import java.util.UUID;

public class Friends {
    public static final Friends INSTANCE = new Friends();

    public static Setting<ArrayList<Friend>> friends;

    public static boolean isFriend(String name) {
        return friends.getValue().stream().anyMatch(friend -> friend.username.equalsIgnoreCase(name));
    }


    public static class Friend {
        String username;

        public Friend(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }
}
