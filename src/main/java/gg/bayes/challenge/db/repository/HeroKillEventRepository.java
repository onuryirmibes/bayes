package gg.bayes.challenge.db.repository;

import gg.bayes.challenge.db.entity.HeroKillEvent;
import gg.bayes.challenge.db.entity.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface HeroKillEventRepository extends CrudRepository<HeroKillEvent, UUID> {
    List<HeroKillEvent> findByMatch(Match match);
}
