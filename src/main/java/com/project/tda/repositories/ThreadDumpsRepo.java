package com.project.tda.repositories;

import com.project.tda.models.ThreadDumps;
import org.springframework.data.repository.CrudRepository;

public interface ThreadDumpsRepo extends CrudRepository<ThreadDumps, Integer> {
}
