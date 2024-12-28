package com.bensiebert.codeup.cws.abstraction;

public class GitModels {

    public static class Commit {
        public String message;
        public String author;
        public String date;
        public String hash;

        public Commit(String message, String author, String date, String hash) {
            this.message = message;
            this.author = author;
            this.date = date;
            this.hash = hash;
        }
    }
}
