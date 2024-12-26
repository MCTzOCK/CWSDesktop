package com.bensiebert.codeup.cws.abstraction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CodeUpModels {

    public static class Repository {
        public String _id;

        public String user;

        public String username;

        public String name;

        public String description;

        public boolean isPublic;

        public String deploymentType;

        public String deploymentName;

        public String createdAt;

        public Integer __v;
    }
}
