package com.portfolio.service;

import com.portfolio.model.Player;
import com.portfolio.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository repo;

    // GET
    public List<Player> findAll() {
        return repo.findAll();
    }

    // GET
    public Player findById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Player not found."));
    }

    // POST
    public Player create(Player pyr) {
        return repo.save(pyr);
    }

    // PUT
    public Player update(String id, Player pyr) {
        Player existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Object not found."));
        existing.setName(pyr.getName());
        existing.setImgSrc(pyr.getImgSrc());
        existing.setLink(pyr.getLink());
        existing.setCategory(pyr.getCategory());

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
