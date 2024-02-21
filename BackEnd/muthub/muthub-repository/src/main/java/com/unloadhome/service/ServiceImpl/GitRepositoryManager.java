package com.unloadhome.service.ServiceImpl;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitRepositoryManager {
    public void createRepo(String localPath) throws IOException, GitAPIException {
        Repository repo = FileRepositoryBuilder.create(new File(localPath, ".git"));
        repo.create();
    }
}
