package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.components.CommitGraph;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.util.EditorDetector;
import com.bensiebert.codeup.cws.util.StreamUtil;
import com.github.weisj.darklaf.components.button.JSplitButton;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class ProjectScreen extends JFrame {

    public CodeUpModels.Repository repo;

    public ProjectScreen(CodeUpModels.Repository r) {
        repo = r;
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(r.name);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Projekt");
        fileMenu.setFont(Fonts.TEXT_FONT);
        JMenuItem closeItem = new JMenuItem("Schließen");
        closeItem.addActionListener(e -> {
            this.dispose();
            new MainScreen();
        });
        closeItem.setFont(Fonts.TEXT_FONT);
        closeItem.setIcon(
                IconFontSwing.buildIcon(FontAwesome.TIMES, 16, Color.WHITE)
        );
        JMenuItem openInExplorer = new JMenuItem("Im Explorer öffnen");
        openInExplorer.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new File(System.getProperty("user.home") + "/codeup/" + r._id).toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        openInExplorer.setFont(Fonts.TEXT_FONT);
        openInExplorer.setIcon(
                IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN, 16, Color.WHITE)
        );
        JMenuItem openInBrowser = new JMenuItem("Im Browser öffnen");
        openInBrowser.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://codeup.space/u/" + r.username + "/" + r.name));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        openInBrowser.setIcon(
                IconFontSwing.buildIcon(FontAwesome.GLOBE, 16, Color.WHITE)
        );
        openInBrowser.setFont(Fonts.TEXT_FONT);

        fileMenu.add(openInExplorer);
        fileMenu.add(openInBrowser);
        fileMenu.add(closeItem);

        JMenu deployMenu = new JMenu("Veröffentlichen");
        deployMenu.setFont(Fonts.TEXT_FONT);
        JMenuItem deployItem = new JMenuItem("Zentrale öffnen");
        deployItem.setIcon(
                IconFontSwing.buildIcon(FontAwesome.ROCKET, 16, Color.WHITE)
        );
        deployItem.setFont(Fonts.TEXT_FONT);
        deployItem.addActionListener(e -> {
            new DeployProjectDialog(this, r);
        });

        deployMenu.add(deployItem);

        JMenu statsMenu = new JMenu("Statistiken");
        statsMenu.setFont(Fonts.TEXT_FONT);
        JMenuItem statsItem = new JMenuItem("Öffnen");
        statsItem.setIcon(
                IconFontSwing.buildIcon(FontAwesome.BAR_CHART, 16, Color.WHITE)
        );
        statsItem.setFont(Fonts.TEXT_FONT);
        statsItem.addActionListener(e -> {
            new StatsDialog(r, this);
        });

        statsMenu.add(statsItem);

        menuBar.add(fileMenu);
        menuBar.add(deployMenu);
        menuBar.add(statsMenu);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fillx", "[]", "[]"));

        JLabel title = new JLabel(r.name);
        title.setFont(Fonts.TITLE_FONT);
        panel.add(title, "wrap");

        JLabel description = new JLabel(r.description);
        description.setFont(Fonts.TEXT_FONT);
        panel.add(description, "wrap");

        JPopupMenu editMenu = new JPopupMenu("Öffnen in");

        ArrayList<EditorDetector.Editor> editors = EditorDetector.getInstalledEditors();
        for (EditorDetector.Editor editor : editors) {
            JMenuItem editorItem = new JMenuItem(editor.type.toString());
            editorItem.setFont(Fonts.TEXT_FONT);
            editorItem.setIcon(
                    new ImageIcon(
                            editor.type.getIcon().getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
                    )
            );
            editorItem.addActionListener(e -> {
                try {
                    String cmd = EditorDetector.getShell() + "\"" + editor.path + "\" " + System.getProperty("user.home") + "/codeup/" + r._id;
                    Process p = Runtime.getRuntime().exec(cmd);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            editMenu.add(editorItem);
        }

        JSplitButton editSplitButton = new JSplitButton("Öffnen in");
        editSplitButton.setFont(Fonts.TEXT_FONT);
        editSplitButton.setActionMenu(editMenu);
        panel.add(editSplitButton, "wrap");

        JLabel git = new JLabel("Git");
        git.setFont(Fonts.TITLE_FONT);

        JScrollPane gitScroll = new JScrollPane();
        gitScroll.setPreferredSize(new Dimension(800, 400));
        gitScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gitScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        CommitGraph graph = new CommitGraph(r);
        gitScroll.setViewportView(graph);

        panel.add(git, "wrap");
        panel.add(graph, "wrap, growx");



        this.setJMenuBar(menuBar);

        this.add(panel);

        this.setVisible(true);
    }
}
