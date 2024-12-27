package com.bensiebert.codeup.cws.util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EditorDetector {

    public static String getBaseCommand() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "cmd.exe /c where ";
        } else {
            return "which ";
        }
    }

    public static String getShell() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "cmd.exe /c ";
        } else {
            return "/bin/bash ";
        }
    }

    public static ArrayList<Editor> getInstalledEditors() {
        ArrayList<Editor> editors = new ArrayList<>();

        String[] editorCommands = {
            "code",
            "idea",
            "eclipse",
            "vim",
            "emacs",
            "atom",
            "webstorm",
        };

        for (String editorCommand : editorCommands) {
            if (isInstalled(editorCommand)) {
                Editor editor = new Editor(EditorType.fromString(editorCommand), getEditorPath(EditorType.fromString(editorCommand)));
                editors.add(editor);
            }
        }

        return editors;
    }

    public static boolean isInstalled(String editorCommand) {
        try {
            Process p = Runtime.getRuntime().exec(getBaseCommand() + editorCommand);
            p.waitFor();
            return p.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEditorPath(EditorType editor) {
        try {
            String cmd = getBaseCommand() + editor.toString().toLowerCase();
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            return p.exitValue() == 0 ? StreamUtil.inputStreamToString(p.getInputStream()).split("\n")[0] : null;
        } catch (Exception e) {
            return null;
        }
    }


    public enum EditorType {
        VSCODE,
        INTELLIJ,
        ECLIPSE,
        VIM,
        EMACS,
        ATOM,
        WEBSTORM,
        UNKNOWN;

        public static EditorType fromString(String editor) {
            return switch (editor) {
                case "code" -> VSCODE;
                case "idea" -> INTELLIJ;
                case "eclipse" -> ECLIPSE;
                case "vim" -> VIM;
                case "emacs" -> EMACS;
                case "atom" -> ATOM;
                case "webstorm" -> WEBSTORM;
                default -> UNKNOWN;
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case VSCODE -> "code";
                case INTELLIJ -> "idea";
                case ECLIPSE -> "eclipse";
                case VIM -> "vim";
                case EMACS -> "emacs";
                case ATOM -> "atom";
                case WEBSTORM -> "webstorm";
                default -> "unknown";
            };
        }

        public ImageIcon getIcon() {
            return switch (this) {
                case VSCODE -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/vscode.png")));
                case INTELLIJ -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/intellij.png")));
                case ECLIPSE -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/eclipse.png")));
                case VIM -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/vim.png")));
                case EMACS -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/emacs.png")));
                case ATOM -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/atom.png")));
                case WEBSTORM -> new ImageIcon(Objects.requireNonNull(EditorDetector.class.getResource("/icons/webstorm.png")));
                default -> null;
            };
        }
    }

    public static class Editor {
        public EditorType type;
        public String path;

        public Editor(EditorType type, String path) {
            this.type = type;
            this.path = path;
        }
    }
}
