package com.unloadhome;

import com.unloadhome.common.RepoVisible;
import org.apache.catalina.User;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

public class MuthubRepoTest {
    @Test
    public void newRepo() throws IOException, IllegalStateException, GitAPIException {
        File localPath = File.createTempFile("testGitRepo", "", new File("F:/"));
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

    @Test
    public void BuildRepo() throws IOException, IllegalStateException, GitAPIException {
        File path = new File(
                "F:" + File.separator + "MutHub" + File.separator +
                        "TuNNan" + File.separator +
                        "Muthubtest1" + File.separator +
                        ".git"
        );
        File UserPath = path.getParentFile();
        if(!UserPath.exists()){
            try{
                UserPath.mkdirs();
            }catch (SecurityException ex){
                throw new IOException(ex.getMessage());
            }
        }

        try {
            Git git = Git.init().setDirectory(UserPath).call();
            System.out.println("Having repo: " + git.getRepository().getDirectory());
        }
        catch (GitAPIException ex){
            throw new RuntimeException(ex);
        }

    }

    @Test
    public void AddFile() throws IOException{
        File path = new File(
                "F:" + File.separator + "MutHub" + File.separator +
                        "TuNNan" + File.separator +
                        "MuthubtestAdd2" + File.separator +
                        ".git"
        );
        File UserPath = path.getParentFile();
        if(!UserPath.exists()){
            try{
                UserPath.mkdirs();
            }catch (SecurityException ex){
                throw new IOException(ex.getMessage());
            }
        }

        try {
            Git git = Git.init().setDirectory(UserPath).call();
            System.out.println("Having repo: " + git.getRepository().getDirectory());
            File fileToAdd = new File(git.getRepository().getDirectory().getParent(), "main.h");

            if(!fileToAdd.createNewFile()){
                throw new IOException("can't create file");
            }
            git.add()
                    .addFilepattern("main.h")
                    .call();
//            git.commit().setMessage("add filetoadd.md").call();
        }
        catch (GitAPIException ex){
            throw new RuntimeException(ex);
        }

    }

    @Test
    public void CommitAll() throws IOException {
        File path = new File(
                "F:" + File.separator + "MutHub" + File.separator +
                        "TuNNan" + File.separator +
                        "MuthubtestAdd2" + File.separator +
                        ".git"
        );
        File UserPath = path.getParentFile();
        if(!UserPath.exists()){
            try{
                UserPath.mkdirs();
            }catch (SecurityException ex){
                throw new IOException(ex.getMessage());
            }
        }

        try {
            Git git = Git.init().setDirectory(UserPath).call();
            System.out.println("Having repo: " + git.getRepository().getDirectory());
            git.add().addFilepattern(".").call();
            git.add().addFilepattern(".").setUpdate(true).call();
            git.commit().setMessage("commit all changes including additions").call();
        }catch (GitAPIException ex){

        }
    }

    @Test
    public void DeleteFile() throws IOException{
        String filename = "main.h";
        File path = new File(
                "F:" + File.separator + "MutHub" + File.separator +
                        "TuNNan" + File.separator +
                        "MuthubtestAdd2" + File.separator +
                        filename
        );
        File RepoPath = path.getParentFile();
        if(path.isFile() && path.exists()){
            path.delete();
        }
        try {
            Git git = Git.init().setDirectory(RepoPath).call();
            git.commit().setMessage("delete file" + filename).call();
        }catch (GitAPIException ex){

        }
        System.out.println("succ to delete file and commit");
    }


}
