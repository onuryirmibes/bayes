package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

import java.util.List;

public interface MatchService {

    /**
     * Takes dota match logs and saves the information to database.
     *
     * @param payload
     * @return {@link Long} match id
     */
    Long ingestMatch(String payload);

    /**
     * Returns list of kill count for each hero for given match id.
     *
     * @param matchId
     * @return {@link List} of {@link HeroKills}
     */
    List<HeroKills> getHeroKills(Long matchId);

    /**
     * Returns every item information purchased by given hero in given match.
     *
     * @param matchId
     * @param heroName
     * @return {@link List} of {@link HeroItems}
     */
    List<HeroItems> getHeroItems(Long matchId, String heroName);

    /**
     * Returns each spell cast information casted by given hero in given match
     *
     * @param matchId
     * @param heroName
     * @return {@link List} of {@link HeroSpells}
     */
    List<HeroSpells> getHeroSpells(Long matchId, String heroName);

    /**
     * Returns all the damaged information dealt by given hero in given match
     *
     * @param matchId
     * @param heroName
     * @return {@link List} of {@link HeroDamage}
     */
    List<HeroDamage> getHeroDamage(Long matchId, String heroName);
}
