package com.bensiebert.codeup.cws.util;

import com.bensiebert.codeup.cws.abstraction.CodeUpModels;
import com.bensiebert.codeup.cws.config.LoginConfig;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

public class ProjectUtil {

    public static void openProject(CodeUpModels.Repository repo) {
        if (hasLocalCopy(repo._id)) {
            openProject(repo._id);
        } else {
            try {
                cloneProject(repo);
                openProject(repo._id);
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
    }

    public static void openProject(String id) {
        System.out.println("Opening project " + id);
    }

    public static void cloneProject(CodeUpModels.Repository repo) throws GitAPIException {
        UsernamePasswordCredentialsProvider ua = new UsernamePasswordCredentialsProvider(LoginConfig.getInstance().username, LoginConfig.getInstance().password);

        String cmd = "git clone https://" + LoginConfig.getInstance().username + ":" + LoginConfig.getInstance().password + "@git.codeup.space/" + repo.username + "/" + repo.name + " " + System.getProperty("user.home") + "/codeup/" + repo._id;

        System.out.println("Cloning project " + repo.name + " from " + repo.username);
        Runtime rt = Runtime.getRuntime();
        try {
            Process p = rt.exec(cmd);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasLocalCopy(String id) {
        File f = new File(System.getProperty("user.home") + "/codeup/" + id);

        return f.exists() && f.isDirectory();
    }

}
