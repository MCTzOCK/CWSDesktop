package com.bensiebert.codeup.cws.ui;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.constants.Fonts;
import com.bensiebert.codeup.cws.util.StreamUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatsDialog extends JDialog {

    private CodeUpModels.Repository repo;
    private Statistics stats;
    private static final String[] ignore = new String[]{
            ".git", ".idea", "node_modules", "build",
            "dist", "target", ".png", ".lock", ".jar",
            "package-lock.json", "yarn.lock", ".iml",
            ".class", ".DS_Store", ".gitignore", ".gitkeep",
            ".gitattributes", ".editorconfig", ".vscode",
            ".classpath", ".project", ".settings", ".gradle",
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".ico",
            ".svg", ".mp4", ".mp3", ".wav", ".flac", ".ogg",
            ".webm", ".avi", ".mov", ".wmv", ".mkv", ".flv",
            ".zip", ".rar", ".7z", ".tar", ".gz", ".bz2",
            ".xz", ".pdf", ".doc", ".docx", ".xls", ".xlsx",
            ".ppt", ".pptx", ".odt", ".ods", ".odp",
    };

    public StatsDialog(CodeUpModels.Repository repo, JFrame parent) {
        super(parent, "Statistiken", true);

        File rootDir = new File(System.getProperty("user.home") + "/codeup/" + repo._id);
        this.calculate(rootDir);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("flowy", "[]", "[]"));
        JLabel title = new JLabel("Statistiken für " + repo.name);
        title.setFont(Fonts.TITLE_FONT);

        JLabel files = new JLabel("Dateien: " + stats.fileCount);
        files.setFont(Fonts.TEXT_FONT);
        JLabel lines = new JLabel("Zeilen: " + stats.lineCount);
        lines.setFont(Fonts.TEXT_FONT);
        JLabel chars = new JLabel("Zeichen: " + stats.charCount);
        chars.setFont(Fonts.TEXT_FONT);

        panel.add(title);
        panel.add(files);
        panel.add(lines);
        panel.add(chars);

        this.setContentPane(panel);
        this.setSize(400, 170);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    public void calculate(File root) {
        try {
            Statistics stats = calculateStats(root);
            this.stats = stats;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Statistics calculateStats(File root) throws IOException {
        int fileCount = 0;
        int lineCount = 0;
        int charCount = 0;

        for (File file : root.listFiles()) {
            // check if path contains any of the ignore strings and skip if it does
            Path path = file.toPath();
            if(anyMatch(path.toString(), ignore)) {
                continue;
            }
            if (file.isDirectory()) {
                Statistics subStats = calculateStats(file);
                fileCount += subStats.fileCount;
                lineCount += subStats.lineCount;
                charCount += subStats.charCount;
            } else {
                fileCount++;
                lineCount += Files.lines(file.toPath()).count();
                charCount += Files.readAllBytes(file.toPath()).length;
            }
        }
        return new Statistics(fileCount, lineCount, charCount);
    }

    private boolean anyMatch(String path, String[] ignore) {
        for (String s : ignore) {
            if (path.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static class Statistics {
        public int fileCount;
        public int lineCount;
        public int charCount;

        public Statistics(int fileCount, int lineCount, int charCount) {
            this.fileCount = fileCount;
            this.lineCount = lineCount;
            this.charCount = charCount;
        }
    }
}
