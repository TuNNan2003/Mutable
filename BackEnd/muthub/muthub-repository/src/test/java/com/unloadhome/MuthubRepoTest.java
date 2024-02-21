package com.unloadhome;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;


public class MuthubRepoTest {
    @Test
    public void newRepo() throws IOException, IllegalStateException, GitAPIException {
        File localPath = File.createTempFile("testGitRepo", "");
        if(!localPath.delete()){
            throw new IOException("Couldn't delete temporary file" + localPath);
        }

        try{
            Git git = Git.init().setDirectory(localPath).call();
            System.out.println("Having repo: " + git.getRepository().getDirectory());
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

    }
}
