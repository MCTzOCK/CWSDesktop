package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.config.LoginConfig;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.rest.HttpUtils;
import com.bensiebert.codeup.cws.util.StreamUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class DeployProjectDialog extends JDialog {

    JLabel currentStatus = new JLabel("Warten...");
    JTextArea logs = new JTextArea();
    CodeUpModels.Repository repo;

    public DeployProjectDialog(JFrame parent, CodeUpModels.Repository repo) {
        super(parent, "Projekt veröffentlichen", true);
        this.repo = repo;
        this.setSize(500, 500);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("flowy, fillx", "[]", "[]"));

        // right top corner
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton reloadButton = new JButton(
                IconFontSwing.buildIcon(FontAwesome.REFRESH, 16, Color.WHITE)
        );

        reloadButton.addActionListener(e -> update());

        JButton deployButton = new JButton(
                IconFontSwing.buildIcon(FontAwesome.ROCKET, 16, Color.WHITE)
        );
        deployButton.addActionListener(e -> {
            JsonNode deployN = HttpUtils.post("https://codeup.space/api/repos/" + this.repo._id + "/deploy", "", LoginConfig.getInstance().token);
            if(deployN.has("error")) {
                JOptionPane.showMessageDialog(this, "Fehler: " + deployN.get("error").asText(), "Fehler", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erfolgreich veröffentlicht!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton openButton = new JButton(
                IconFontSwing.buildIcon(FontAwesome.EXTERNAL_LINK, 16, Color.WHITE)
        );
        openButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://" + this.repo.deploymentName + ".user-content.dev"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonPanel.add(reloadButton);
        buttonPanel.add(deployButton);
        buttonPanel.add(openButton);

        panel.add(buttonPanel, "growx");


        JEditorPane description = new JEditorPane();
        description.setContentType("text/html;charset=UTF-8");
        description.setEditable(false);
        description.setSelectionColor(description.getBackground());
        description.setText(StreamUtil.inputStreamToString(
                getClass().getResourceAsStream("/html/deploy.html")
        ));

        panel.add(description, "growx");

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new MigLayout("flowy, fillx", "[]", "[]10[]"));

        JLabel statusLabel = new JLabel("Status");
        statusLabel.setFont(Fonts.SUBTITLE_FONT);

        statusPanel.add(statusLabel, "growx");
        statusPanel.add(this.currentStatus, "growx");

        panel.add(statusPanel, "growx");

        JPanel logsPanel = new JPanel();
        logsPanel.setLayout(new MigLayout("flowy, fillx", "[]", "[]10[]"));

        JLabel logsLabel = new JLabel("Logs");
        logsLabel.setFont(Fonts.SUBTITLE_FONT);

        logs.setEditable(false);

        JScrollPane logsScroll = new JScrollPane(logs);
        logsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        logsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        logsPanel.add(logsLabel, "growx");
        logsPanel.add(logsScroll, "growx, growy");

        panel.add(logsPanel, "growx, growy");

        add(panel);

        update();

        setVisible(true);
    }

    public void update() {
        JsonNode statusN = HttpUtils.get("https://codeup.space/api/repos/" + this.repo._id + "/deploy/status", LoginConfig.getInstance().token);
        if(statusN.has("error")) {
            this.currentStatus.setText("Fehler: " + statusN.get("error").asText());
        } else {
            this.currentStatus.setText(statusN.get("containerStatus").asText());
        }

        JsonNode logsN = HttpUtils.get("https://codeup.space/api/repos/" + this.repo._id + "/deploy/logs", LoginConfig.getInstance().token);
        if(logsN.has("error")) {
            this.logs.setText("Fehler: " + logsN.get("error").asText());
        } else {
            this.logs.setText(logsN.get("logs").asText());
        }

        this.repaint();
    }
}
