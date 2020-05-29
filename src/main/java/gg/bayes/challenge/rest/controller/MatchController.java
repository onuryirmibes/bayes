package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * Takes dota match logs and saves the information to database.
     *
     * @param payload
     * @return {@link Long} match id
     */
    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        final Long matchId = matchService.ingestMatch(payload);
        log.info("Ingest match completed for the match id : {}", matchId);
        return ResponseEntity.ok(matchId);
    }

    /**
     * Returns list of kill count for each hero for given match id.
     *
     * @param matchId
     * @return {@link List} of {@link HeroKills}
     */
    @GetMapping("{matchId}")
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
        log.info("Get hero kills initiated for match : {}", matchId);
        final List<HeroKills> heroKills = matchService.getHeroKills(matchId);
        return ResponseEntity.ok(heroKills);
    }

    /**
     * Returns every item information purchased by given hero in given match.
     *
     * @param matchId
     * @param heroName
     * @return {@link List} of {@link HeroItems}
     */
    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
                                                    @PathVariable("heroName") String heroName) {
        log.info("Get hero items initiated for match : {} and hero : {}", matchId, heroName);
        final List<HeroItems> heroItems = matchService.getHeroItems(matchId, heroName);
        return ResponseEntity.ok(heroItems);
    }

    /**
     * Returns each spell cast information casted by given hero in given match
     *
     * @param matchId
     * @param heroName
     * @return {@link List} of {@link HeroSpells}
     */
    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        log.info("Get hero spells initiated for match : {} and hero : {}", matchId, heroName);
        final List<HeroSpells> heroSpells = matchService.getHeroSpells(matchId, heroName);
        return ResponseEntity.ok(heroSpells);
    }

    /**
     * Returns all the damaged information dealt by given hero in given match
     *
     * @param matchId
     * @param heroName
     * @return {@link List} of {@link HeroDamage}
     */
    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        log.info("Get hero damage initiated for match : {} and hero : {}", matchId, heroName);
        final List<HeroDamage> heroDamage = matchService.getHeroDamage(matchId, heroName);
        return ResponseEntity.ok(heroDamage);
    }
}
