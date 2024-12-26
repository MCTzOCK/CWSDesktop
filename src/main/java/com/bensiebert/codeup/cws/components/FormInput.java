package com.bensiebert.codeup.cws.components;

import com.bensiebert.codeup.cws.constants.Fonts;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class FormInput extends JComponent {

    private JTextField input;

    public FormInput(String label) {
        setBackground(Color.WHITE);
        setLayout(new MigLayout("fillx, insets 0", "[grow, fill]", "[]10[]10"));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(Fonts.SUBTITLE_FONT);
        input = new JTextField();
        input.setFont(Fonts.TEXT_FONT);


        add(labelComponent, "wrap");
        add(input, "growx");
    }

    public void setText(String text) {
        input.setText(text);
    }

    public String getText() {
        return input.getText();
    }
}
