package com.bensiebert.codeup.cws.util;

import java.util.ArrayList;

public class EditorDetector {

    public static String getBaseCommand() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "cmd.exe /c where ";
        } else {
            return "which ";
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
            "subl",
            "notepad",
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
            System.out.println(cmd);
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
        SUBLIME,
        NOTEPAD,
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
                case "subl" -> SUBLIME;
                case "notepad" -> NOTEPAD;
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
                case SUBLIME -> "subl";
                case NOTEPAD -> "notepad";
                case WEBSTORM -> "webstorm";
                default -> "unknown";
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
