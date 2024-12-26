package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.components.FormInput;
import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.rest.HttpUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class UpdateProjectDialog extends JDialog {

    public UpdateProjectDialog(JFrame parent, Function<Void, Void> reload, CodeUpModels.Repository repo) {
        super(parent, "Projekt bearbeiten", true);
        this.setSize(500, 500);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("flowy, fillx", "[]", "[]10[]"));
        FormInput description = new FormInput("Beschreibung");
        description.setText(repo.description);

        panel.add(description, "growx");

        JLabel typeLabel = new JLabel("Projekttyp");
        typeLabel.setFont(Fonts.SUBTITLE_FONT);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        ButtonGroup buttonGroup = new ButtonGroup();

        JToggleButton staticButton = new JToggleButton("Statisch");
        JToggleButton dynamicButton = new JToggleButton("Dynamisch");

        if(repo.deploymentType.equals("static")) {
            staticButton.setSelected(true);
        } else {
            dynamicButton.setSelected(true);
        }

        buttonGroup.add(staticButton);
        buttonGroup.add(dynamicButton);

        buttonPanel.add(staticButton);
        buttonPanel.add(dynamicButton);

        JLabel visibilityLabel = new JLabel("Sichtbarkeit");
        visibilityLabel.setFont(Fonts.SUBTITLE_FONT);

        JCheckBox visibilityButton = new JCheckBox("Öffentlich");
        visibilityButton.setSelected(false);
        visibilityButton.setFont(Fonts.TEXT_FONT);

        if(repo.isPublic) {
            visibilityButton.setSelected(true);
        }

        FormInput deployUrl = new FormInput("Veröffentlichungs-URL ([Projektname].user-content.dev)");
        deployUrl.setText(repo.deploymentName);

        panel.add(visibilityLabel, "growx");
        panel.add(visibilityButton, "growx");
        panel.add(typeLabel, "growx");
        panel.add(buttonPanel, "growx");
        panel.add(deployUrl, "growx");

        JButton submit = new JButton("Speichern");
        submit.setFont(Fonts.SUBTITLE_FONT);
        submit.addActionListener(e -> {
            String projectDescription = description.getText();
            String projectType = staticButton.isSelected() ? "static" : "dynamic";
            boolean projectVisibility = visibilityButton.isSelected();
            String projectDeployUrl = deployUrl.getText();

            if (projectDescription.isEmpty() || projectDeployUrl.isEmpty()
            ) {
                JOptionPane.showMessageDialog(this, "Bitte füll alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // create body using jackson
            ObjectNode body = new ObjectMapper().createObjectNode();
            body.put("description", projectDescription);
            body.put("deploymentType", projectType);
            body.put("isPublic", projectVisibility);
            body.put("deploymentName", projectDeployUrl);


            String b = body.toString();

            JsonNode n = HttpUtils.request("https://codeup.space/api/v2/repos/u/" + repo.username + "/" + repo.name, "POST", b, LoginConfig.getInstance().token);

            if(n.has("error")) {
                JOptionPane.showMessageDialog(this, "Fehler beim Bearbeiten des Projekts: " + n.get("error").asText(), "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Projekt erfolgreich bearbeitet.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();

            reload.apply(null);

        });

        panel.add(submit, "growx");

        add(panel);

        this.setVisible(true);
    }
}
