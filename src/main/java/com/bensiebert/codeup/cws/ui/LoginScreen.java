package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.rest.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {

    public LoginScreen() {
        JFrame jf = new JFrame("Login");
        jf.setSize(500, 450);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        JPanel jp = new JPanel();
        jp.setLayout(null);

        JLabel jl = new JLabel("Anmelden");
        jl.setBounds(50, 25, 300, 50);
        jl.setFont(Fonts.TITLE_FONT);
        jp.add(jl);

        JLabel usernameLabel = new JLabel("Benutzername");
        usernameLabel.setBounds(50, 100, 300, 50);
        usernameLabel.setFont(Fonts.SUBTITLE_FONT);
        jp.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 150, 400, 35);
        usernameField.setFont(Fonts.TEXT_FONT);
        jp.add(usernameField);

        JLabel passwordLabel = new JLabel("Passwort");
        passwordLabel.setBounds(50, 200, 300, 50);
        passwordLabel.setFont(Fonts.SUBTITLE_FONT);
        jp.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 250, 400, 35);
        passwordField.setFont(Fonts.TEXT_FONT);
        jp.add(passwordField);

        JButton loginButton = new JButton("Anmelden");
        loginButton.setBounds(50, 350, 400, 50);
        loginButton.setFont(Fonts.SUBTITLE_FONT);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

                JsonNode n = HttpUtils.request("https://codeup.space/api/account/login", "POST", body, "");

                if(n.get("token") != null && n.get("error") == null) {
                    LoginConfig.getInstance().token = n.get("token").asText();
                    LoginConfig.getInstance().update();
                } else {
                    if(n.get("_2fa") != null) {
                        String code = JOptionPane.showInputDialog(jf, "2FA Code eingeben","", JOptionPane.QUESTION_MESSAGE);
                        String body2fa = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"code\": \"" + code + "\" }";

                        JsonNode n2fa = HttpUtils.request("https://codeup.space/api/account/login", "POST", body2fa, "");

                        if(n2fa.get("token") != null && n2fa.get("error") == null) {
                            LoginConfig.getInstance().token = n2fa.get("token").asText();
                            LoginConfig.getInstance().update();
                        } else {
                            JOptionPane.showMessageDialog(jf, "Fehler beim Anmelden", "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        jp.add(loginButton);


        jf.add(jp);

        jf.setVisible(true);
    }
}
