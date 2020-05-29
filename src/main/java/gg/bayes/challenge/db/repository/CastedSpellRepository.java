package gg.bayes.challenge.db.repository;

import gg.bayes.challenge.db.entity.CastedSpell;
import gg.bayes.challenge.db.entity.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CastedSpellRepository extends CrudRepository<CastedSpell, UUID> {
    List<CastedSpell> findByMatchAndHero(Match match, String heroName);
}
