package blu3.FyreHack.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Friends {
    List<String> names = new ArrayList<>();
    public Friends() {
        try {
            List<String> friends = FileMang.readFileLines("friends.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) friends.stream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                names.add(inputLine);
                FyreLogger.log(inputLine);
            }
        } catch (Exception e) {}
    }

    public boolean isFriend(String name){
        return names.contains(name);
    }



    // how 2 friends: FyreHack.getInstance().friends.isFriend(username)
}
