package com.bensiebert.codeup.cws;

import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.ui.LoginScreen;
import com.bensiebert.codeup.cws.ui.MainScreen;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.OneDarkTheme;

public class Main {

    public static void main(String[] args) {
        LafManager.setDecorationsEnabled(true);
        LafManager.installTheme(new OneDarkTheme());


        LoginConfig.init();
        LoginConfig.getInstance().update();

        if (LoginConfig.getInstance().token == null) {
            new LoginScreen();
        } else {
            new MainScreen();
        }
    }
}
