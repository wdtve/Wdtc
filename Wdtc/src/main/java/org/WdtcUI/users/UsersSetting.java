package org.WdtcUI.users;

import java.util.ArrayList;
import java.util.List;

public class UsersSetting {
    public static List<String> users_jvm = new ArrayList<>();
    public static List<String> user_game = new ArrayList<>();
    public List<String> getUsers_jvm() {
        return users_jvm;
    }

    public void setUsers_jvm(String users_jvm) {
        UsersSetting.users_jvm.add(users_jvm);
    }

    public List<String> getUser_game() {
        return user_game;
    }

    public void setUser_game(String user_game) {
        UsersSetting.user_game.add(user_game);
    }
}
