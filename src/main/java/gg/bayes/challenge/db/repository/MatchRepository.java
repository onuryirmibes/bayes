package gg.bayes.challenge.db.repository;

import gg.bayes.challenge.db.entity.Match;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, Long> {
}
