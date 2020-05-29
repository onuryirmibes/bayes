package gg.bayes.challenge.db.repository;

import gg.bayes.challenge.db.entity.HeroDamageEvent;
import gg.bayes.challenge.db.entity.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface HeroDamageEventRepository extends CrudRepository<HeroDamageEvent, UUID> {
    List<HeroDamageEvent> findByMatchAndDealerHero(Match match, String heroName);
}
