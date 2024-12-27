package com.bensiebert.codeup.cws.config;

import com.bensiebert.codeup.cws.rest.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;

public class LoginConfig implements Serializable {

    public static final String savePath = System.getProperty("user.home") + "/.cwsdesktop-login";

    public String username;
    public String firstName;
    public String lastName;
    public String token;
    public String password;

    public static LoginConfig instance;

    public static LoginConfig getInstance() {
        return instance;
    }

    public static void setInstance(LoginConfig instance) {
        LoginConfig.instance = instance;
    }


    public void save() {
        try {
            System.out.println("Saving login config");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            instance = (LoginConfig) new ObjectInputStream(new FileInputStream(savePath)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        File f = new File(savePath);
        if (f.exists()) {
            load();
        } else {
            instance = new LoginConfig();
        }
    }

    public void update() {
        if(this.token == null) return;

        JsonNode n = HttpUtils.get("https://codeup.space/api/account/verify", this.token);

        if(!n.get("verified").asBoolean()) {
            this.token = null;
            this.save();
            return;
        }

        if(n.get("token") != null) {
            this.token = n.get("token").asText();
            this.update();
            return;
        }

        this.username = n.get("data").get("username").asText();
        this.firstName = n.get("data").get("firstName").asText();
        this.lastName = n.get("data").get("lastName").asText();

        this.save();
    }

}
