package gg.bayes.challenge.db.repository;

import gg.bayes.challenge.db.entity.Match;
import gg.bayes.challenge.db.entity.PurchasedItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PurchasedItemRepository extends CrudRepository<PurchasedItem, UUID> {
    List<PurchasedItem> findByMatchAndHero(Match match, String heroName);
}
