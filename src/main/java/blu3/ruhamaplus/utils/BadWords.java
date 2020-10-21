package blu3.ruhamaplus.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BadWords {

    private static final List<String> badWords = Arrays.asList(
            "nigger",
            "fuck",
            "shit",
            "bitch",
            "nigga",
            "retard",
            "faggot",
            "fag",
            "newfag",
            "tranny",
            "trannies",
            "niggers",
            "niggas"
    );


    public static List<String> getBadWords() {
        return badWords;
    }
}
