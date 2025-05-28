package com.portfolio.service;

import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repo;

    // GET
    public List<Project> findAll() {
        return repo.findAll();
    }

    // GET
    public Project findById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Project not found."));
    }

    // POST
    public Project create(Project proj) {
        return repo.save(proj);
    }

    // PUT
    public Project update(String id, Project proj) {
        Project existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Object not found."));
        existing.setName_pt(proj.getName_pt());
        existing.setName_en(proj.getName_en());
        existing.setImgSrc(proj.getImgSrc());
        existing.setGithub(proj.getGithub());

        return repo.save(existing);
    }

    // DELETE
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Object not found.");
        }

        repo.deleteById(id);
    }
}
