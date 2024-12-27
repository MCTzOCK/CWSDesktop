package com.bensiebert.codeup.cws.components;

import com.bensiebert.codeup.cws.util.ProjectUtil;
import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.rest.HttpUtils;
import com.bensiebert.codeup.cws.ui.UpdateProjectDialog;
import com.fasterxml.jackson.databind.JsonNode;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class RepositoryItem extends JComponent {

    public CodeUpModels.Repository repository;

    public RepositoryItem(JFrame parent, CodeUpModels.Repository repository, Function<Void, Void> reload) {
        this.repository = repository;

        setSize(700, 100);

        setLayout(new MigLayout("wrap 4", "[]20[]push[]", "[]"));

        JLabel visibility = new JLabel(
                repository.isPublic ?
                        IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PUBLIC, 40, new Color(255, 255, 255))
                        : IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VISIBILITY_OFF, 40, new Color(255, 255, 255))
        );

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());

        JLabel name = new JLabel(repository.name);
        name.setFont(Fonts.TITLE_FONT);

        JLabel description = new JLabel(repository.description);
        description.setFont(Fonts.TEXT_FONT);

        textPanel.add(name, BorderLayout.NORTH);
        textPanel.add(description, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton editButton = new JButton(IconFontSwing.buildIcon(FontAwesome.PENCIL, 20, new Color(255, 255, 255)));
        JButton deleteButton = new JButton(IconFontSwing.buildIcon(FontAwesome.TRASH, 20, new Color(255, 255, 255)));
        JButton openButton = new JButton(IconFontSwing.buildIcon(FontAwesome.EXTERNAL_LINK, 20, new Color(255, 255, 255)));

        deleteButton.addActionListener(e -> {
            if(
                    JOptionPane.showConfirmDialog(
                            this,
                            "Möchtest du das Projekt wirklich löschen?",
                            "Projekt löschen",
                            JOptionPane.YES_NO_OPTION
                    ) == JOptionPane.YES_OPTION
            ) {
                JsonNode n = HttpUtils.request("https://codeup.space/api/v2/repos/u/" + repository.username + "/" + repository.name, "DELETE", "", LoginConfig.getInstance().token);
                if(n.has("error")) {
                    JOptionPane.showMessageDialog(this, n.get("error").asText(), "Fehler", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Projekt erfolgreich gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                    reload.apply(null);
                }
            }
        });

        editButton.addActionListener(e -> {
            new UpdateProjectDialog(parent, reload, repository);
        });

        openButton.addActionListener(e -> {
            ProjectUtil.openProject(repository);
            parent.dispose();
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(openButton);

        add(visibility);
        add(textPanel, "span 2");
        add(buttonPanel);
    }


}
