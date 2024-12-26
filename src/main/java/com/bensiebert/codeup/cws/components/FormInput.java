package com.bensiebert.codeup.cws.components;

import com.bensiebert.codeup.cws.constants.Fonts;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class FormInput extends JComponent {

    public String text;

    public FormInput(String label) {
        System.out.println(label);
        setBackground(Color.WHITE);
        setLayout(new MigLayout("fillx, insets 0", "[grow, fill]", "[]10[]10"));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(Fonts.SUBTITLE_FONT);
        JTextField input = new JTextField();
        input.setFont(Fonts.TEXT_FONT);
        input.addActionListener(e -> {
            text = input.getText();
        });


        add(labelComponent, "wrap");
        add(input, "growx");
    }

    public String getText() {
        return text;
    }
}
