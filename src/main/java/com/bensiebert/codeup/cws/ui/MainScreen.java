package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.components.RepositoryItem;
import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.rest.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.weisj.darklaf.settings.ThemeSettings;
import com.github.weisj.darklaf.theme.Theme;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainScreen extends JFrame {

    public ArrayList<CodeUpModels.Repository> projects = new ArrayList<>();
    public JPanel repoPane;

    public MainScreen() {
        super("CodeUp Workspace");
        this.setSize(800, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel jp = new JPanel();
        jp.setLayout(null);

        JLabel jl = new JLabel("Projekte");
        jl.setBounds(50, 25, 300, 50);
        jl.setFont(Fonts.TITLE_FONT);
        jp.add(jl);

        JButton createButton = new JButton(IconFontSwing.buildIcon(FontAwesome.PLUS, 20, new Color(255, 255, 255)));
        createButton.setBounds(700, 25, 35, 35);
        createButton.addActionListener(e -> {
            new CreateProjectDialog(this);
        });

        JButton reloadButton = new JButton(IconFontSwing.buildIcon(FontAwesome.REFRESH, 20, new Color(255, 255, 255)));
        reloadButton.setBackground(Color.BLUE);
        reloadButton.setBounds(650, 25, 35, 35);
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadProjects();
            }
        });

        repoPane = new JPanel();
        repoPane.setLayout(new BoxLayout(repoPane, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(repoPane);
        scrollPane.setBounds(50, 100, 700, 500);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        jp.add(scrollPane);
        jp.add(createButton);
        jp.add(reloadButton);


        this.add(jp);
        this.setVisible(true);

        reloadProjects();
    }

    public void reloadProjects() {
        JsonNode n = HttpUtils.get("https://codeup.space/api/v2/repos/u/" + LoginConfig.getInstance().username, LoginConfig.getInstance().token);

        if(n.has("error")) {
            JOptionPane.showMessageDialog(null, "Fehler beim Laden der Projekte: " + n.get("error").asText());
            return;
        }

        projects.clear();

        JsonNode repos = n.get("data");

        for(JsonNode repo : repos) {
            CodeUpModels.Repository r = new CodeUpModels.Repository();
            r._id = repo.get("_id").asText();
            r.user = repo.get("user").asText();
            r.username = repo.get("username").asText();
            r.name = repo.get("name").asText();
            r.description = repo.get("description").asText();
            r.isPublic = repo.get("public").asBoolean();
            r.deploymentType = repo.get("deploymentType").asText();
            r.deploymentName = repo.get("deploymentName").asText();
            r.createdAt = repo.get("createdAt").asText();
            r.__v = repo.get("__v").asInt();

            projects.add(r);
        }

        repoPane.removeAll();

        for (CodeUpModels.Repository r : projects) {
            RepositoryItem ri = new RepositoryItem(r);
            repoPane.add(ri);
        }

        repoPane.revalidate();
    }
}
