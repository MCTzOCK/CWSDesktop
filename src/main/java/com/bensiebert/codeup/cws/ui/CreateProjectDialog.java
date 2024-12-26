package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.components.FormInput;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CreateProjectDialog extends JDialog {

    public CreateProjectDialog(JFrame parent) {
        super(parent, "Projekt erstellen", true);
        this.setSize(400, 300);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("flowy, fillx", "[]", "[]10[]"));

        panel.add(new FormInput("Name"), "growx");
        panel.add(new FormInput("Beschreibung"), "growx");


        add(panel);

        this.setVisible(true);
    }
}
