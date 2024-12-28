package com.bensiebert.codeup.cws.components;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.abstraction.GitModels;
import com.bensiebert.codeup.cws.constants.Fonts;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class CommitGraph extends JComponent {

    public CodeUpModels.Repository repo;
    public ArrayList<GitModels.Commit> commits = new ArrayList<>();
    public JTable commitTable;
    public JPanel commitPanel;

    public CommitGraph(CodeUpModels.Repository repo) {
        this.repo = repo;
        setLayout(new MigLayout("wrap 2", "[]10[]", "[]"));

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new MigLayout("wrap 3", "[]10[]10[]", "[]"));

        JButton reloadButton = new JButton("Aktualisieren");
        reloadButton.addActionListener(e -> reload());
        actionPanel.add(reloadButton, "growx");
        JButton pushButton = new JButton("Push");
        pushButton.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("git push", null, new File(System.getProperty("user.home") + "/codeup/" + repo._id));
                JOptionPane.showMessageDialog(this, "Push erfolgreich", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        actionPanel.add(pushButton, "growx");

        JButton pullButton = new JButton("Pull");
        pullButton.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("git pull", null, new File(System.getProperty("user.home") + "/codeup/" + repo._id));
                JOptionPane.showMessageDialog(this, "Pull erfolgreich", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        actionPanel.add(pullButton, "growx");

        add(actionPanel, "span 2, growx");

        commitTable = new JTable();
        commitTable.setAutoCreateRowSorter(true);
        commitTable.setFillsViewportHeight(true);
        commitTable.setRowHeight(30);
        commitTable.setFont(Fonts.TEXT_FONT);

        add(new JScrollPane(commitTable), "growx");
        commitPanel = new JPanel();
        commitPanel.setLayout(new MigLayout("fillx, insets 0", "[grow, fill]", "[]"));

        add(commitPanel, "span 2, growx, growy");

        reload();
    }

    public void reload() {
        Runtime runtime = Runtime.getRuntime();

        try {
            Process process = runtime.exec("git log --pretty=format:\"%h##%s##%an##%ad\" --date=default", null, new File(System.getProperty("user.home") + "/codeup/" + repo._id));
            String output = new String(process.getInputStream().readAllBytes());
            String[] lines = output.split("\n");

            commits.clear();

            for (String line : lines) {
                String[] parts = line.split("##");
                commits.add(new GitModels.Commit(parts[1], parts[2], parts[3], parts[0]));
            }

            CommitTableModel model = new CommitTableModel(commits);

            commitTable.setModel(model);

            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Process process = runtime.exec("git diff --name-only", null, new File(System.getProperty("user.home") + "/codeup/" + repo._id));
            process.waitFor();
            String output = new String(process.getInputStream().readAllBytes());
            String[] lines = output.split("\n");
            System.out.println(output);

            commitPanel.removeAll();
            JLabel title = new JLabel("Änderungen");
            title.setFont(Fonts.TEXT_FONT);
            commitPanel.add(title, "wrap");
            for (String line : lines) {
                if(line.startsWith("warning:")) continue;
                commitPanel.add(new JLabel(line), "growx, wrap");
            }

            if(commitPanel.getComponentCount() == 1) {
                commitPanel.add(new JLabel("Keine Änderungen"), "growx, wrap");
            } else {
                JTextField commitMessage = new JTextField();
                commitMessage.setFont(Fonts.TEXT_FONT);
                commitMessage.setToolTipText("Commit-Nachricht");
                commitPanel.add(commitMessage, "growx, wrap");
                JButton commitButton = new JButton("Commit");
                commitButton.addActionListener(e -> {
                    try {
                        Process p = runtime.exec("git add .", null, new File(System.getProperty("user.home") + "/codeup/" + repo._id));
                        p.waitFor();
                        p = runtime.exec("git commit -m \"" + commitMessage.getText() + "\"", null, new File(System.getProperty("user.home") + "/codeup/" + repo._id));
                        p.waitFor();
                        reload();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                commitPanel.add(commitButton, "wrap");

                commitPanel.revalidate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.repaint();
    }

    public static class CommitTableModel extends AbstractTableModel {
        public ArrayList<GitModels.Commit> commits;

        public CommitTableModel(ArrayList<GitModels.Commit> commits) {
            this.commits = commits;
        }

        @Override
        public int getRowCount() {
            return commits.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            GitModels.Commit commit = commits.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> commit.date;
                case 1 -> commit.author;
                case 2 -> commit.message;
                case 3 -> commit.hash;
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return switch (column) {
                case 0 -> "Date";
                case 1 -> "Author";
                case 2 -> "Message";
                case 3 -> "Hash";
                default -> null;
            };
        }
    }
}
