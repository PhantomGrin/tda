package com.project.tda.repositories;

import com.project.tda.models.ThreadDumps;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ThreadDumpsRepo extends CrudRepository<ThreadDumps, Integer> {
    List<ThreadDumps> findAllByUsername(String username);
}
