package com.bensiebert.codeup.cws;

import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.ui.LoginScreen;
import com.bensiebert.codeup.cws.ui.MainScreen;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.OneDarkTheme;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.icons.elusive.Elusive;
import jiconfont.icons.iconic.Iconic;
import jiconfont.icons.entypo.Entypo;
import jiconfont.icons.typicons.Typicons;
import jiconfont.swing.IconFontSwing;

public class Main {

    public static void main(String[] args) {
        LafManager.setDecorationsEnabled(false);
        LafManager.installTheme(new OneDarkTheme());


        LoginConfig.init();
        LoginConfig.getInstance().update();
        IconFontSwing.register(FontAwesome.getIconFont());
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        IconFontSwing.register(Elusive.getIconFont());
        IconFontSwing.register(Iconic.getIconFont());
        IconFontSwing.register(Entypo.getIconFont());
        IconFontSwing.register(Typicons.getIconFont());


        if (LoginConfig.getInstance().token == null) {
            new LoginScreen();
        } else {
            new MainScreen();
        }
    }
}
