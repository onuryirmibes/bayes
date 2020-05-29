package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.db.entity.*;
import gg.bayes.challenge.db.repository.*;
import gg.bayes.challenge.exception.DotaChallangeBadRequestException;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static gg.bayes.challenge.service.util.MatchServiceUtil.*;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PurchasedItemRepository itemRepository;

    @Autowired
    private CastedSpellRepository spellRepository;

    @Autowired
    private HeroDamageEventRepository damageEventRepository;

    @Autowired
    private HeroKillEventRepository killEventRepository;

    @Override
    public Long ingestMatch(final String payload) {
        final Match match = matchRepository.save(new Match());
        final String[] lines = payload.split("\\n");

        for(final String line : lines) {
            final String[] tokens = line.split("\\s+");
            final String heroName = tokens[1];
            if(!heroName.startsWith("npc_dota_hero")) {
                continue;
            }
            if (line.contains(" buys item ")) {
                savePurchasedItem(tokens, match);
            } else if (line.contains(" casts ability ")) {
                saveCastedSpell(tokens, match);
            } else if (line.contains(" hits ")) {
                saveHeroDamageEvent(tokens, match);
            } else if (line.contains(" is killed ")) {
                saveHeroKillEvent(tokens, match);
            }
        }

        return match.getId();
    }

    private void savePurchasedItem(final String[] tokens, final Match match) {
        final String hero = trimHeroName(tokens[1]);
        final String timeStampToken = tokens[0].replaceAll("\\[|\\]", "");
        final String item = tokens[4].replace("item_", "");
        final PurchasedItem purchasedItem = new PurchasedItem();
        purchasedItem.setHero(hero);
        purchasedItem.setItemName(item);
        purchasedItem.setMatch(match);
        purchasedItem.setTimeStamp(convertTimeStamp(timeStampToken));
        itemRepository.save(purchasedItem);
    }

    private void saveCastedSpell(final String[] tokens, final Match match) {
        final String hero = trimHeroName(tokens[1]);
        final String spellName = tokens[4];
        final String spellLevel = tokens[6].replace(")", "");
        final CastedSpell castedSpell = new CastedSpell();
        castedSpell.setHero(hero);
        castedSpell.setSpellName(spellName);
        castedSpell.setSpellLevel(Integer.valueOf(spellLevel));
        castedSpell.setMatch(match);
        spellRepository.save(castedSpell);
    }

    private void saveHeroDamageEvent(final String[] tokens, final Match match) {
        if(!tokens[3].startsWith("npc_dota_hero")) {
            return;
        }
        final String dealerHero = trimHeroName(tokens[1]);
        final String targetHero = trimHeroName(tokens[3]);
        final DamageType damageType = getDamageType(tokens[5]);
        final Integer damageValue = Integer.valueOf(tokens[7]);
        final HeroDamageEvent damageEvent = new HeroDamageEvent();
        damageEvent.setDealerHero(dealerHero);
        damageEvent.setTargetHero(targetHero);
        damageEvent.setType(damageType);
        damageEvent.setValue(damageValue);
        damageEvent.setMatch(match);
        damageEventRepository.save(damageEvent);
    }

    private void saveHeroKillEvent(final String[] tokens, final Match match) {
        if(!tokens[5].startsWith("npc_dota_hero")) {
            return;
        }
        final String targetHero = trimHeroName(tokens[1]);
        final String killerHero = trimHeroName(tokens[5]);
        final HeroKillEvent heroKillEvent = new HeroKillEvent();
        heroKillEvent.setKillerHero(killerHero);
        heroKillEvent.setTargetHero(targetHero);
        heroKillEvent.setMatch(match);
        killEventRepository.save(heroKillEvent);
    }

    private Match getMatch(final Long matchId) {
        final Optional<Match> optionalMatch = matchRepository.findById(matchId);
        if (!optionalMatch.isPresent()) {
            throw new DotaChallangeBadRequestException("Requested id is not found. Id : " + matchId);
        }
        return optionalMatch.get();
    }

    @Override
    public List<HeroKills> getHeroKills(final Long matchId) {
        final Match match = getMatch(matchId);
        final List<HeroKillEvent> heroKillEventList = killEventRepository.findByMatch(match);
        final Map<String, Integer> map = new HashMap<>();
        for (final HeroKillEvent killEvent : heroKillEventList) {
            int count = map.getOrDefault(killEvent.getKillerHero(), 0);
            map.put(killEvent.getKillerHero(), count + 1);
        }
        final List<HeroKills> heroKillsList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            final HeroKills heroKills = new HeroKills();
            heroKills.setHero(entry.getKey());
            heroKills.setKills(entry.getValue());
            heroKillsList.add(heroKills);
        }
        return heroKillsList;
    }

    @Override
    public List<HeroItems> getHeroItems(final Long matchId, final String heroName) {
        final Match match = getMatch(matchId);
        final List<PurchasedItem> purchasedItems = itemRepository.findByMatchAndHero(match, heroName);
        final List<HeroItems> items = new ArrayList<>();
        for (final PurchasedItem purchasedItem : purchasedItems) {
            final HeroItems heroItems = new HeroItems();
            heroItems.setItem(purchasedItem.getItemName());
            heroItems.setTimestamp(purchasedItem.getTimeStamp());
            items.add(heroItems);
        }
        return items;
    }

    @Override
    public List<HeroSpells> getHeroSpells(final Long matchId, final String heroName) {
        final Match match = getMatch(matchId);
        final List<CastedSpell> castedSpells = spellRepository.findByMatchAndHero(match, heroName);
        final Map<String, Integer> map = new HashMap<>();
        for (final CastedSpell castedSpell : castedSpells) {
            int count = map.getOrDefault(castedSpell.getSpellName(), 0);
            map.put(castedSpell.getSpellName(), count + 1);
        }
        final List<HeroSpells> spells = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            final HeroSpells heroSpell = new HeroSpells();
            heroSpell.setSpell(entry.getKey());
            heroSpell.setCasts(entry.getValue());
            spells.add(heroSpell);
        }
        return spells;
    }

    @Override
    public List<HeroDamage> getHeroDamage(final Long matchId, final String heroName) {
        final Match match = getMatch(matchId);
        final List<HeroDamageEvent> heroDamageEvents
                = damageEventRepository.findByMatchAndDealerHero(match, heroName);
        final Map<String, Pair<Integer, Integer>> map = new HashMap<>();
        for (final HeroDamageEvent damageEvent : heroDamageEvents) {
            final String targetHero = damageEvent.getTargetHero();
            if (map.containsKey(targetHero)) {
                final Integer damageInstances = map.get(targetHero).getKey() + 1;
                final Integer totalDamage = map.get(targetHero).getValue() + damageEvent.getValue();
                map.put(targetHero, Pair.of(damageInstances, totalDamage));
            } else {
                map.put(targetHero, Pair.of(1, damageEvent.getValue()));
            }
        }
        final List<HeroDamage> heroDamageList = new ArrayList<>();
        for (Map.Entry<String, Pair<Integer, Integer>> entry : map.entrySet()) {
            final HeroDamage heroDamage = new HeroDamage();
            heroDamage.setTarget(entry.getKey());
            heroDamage.setDamageInstances(entry.getValue().getKey());
            heroDamage.setTotalDamage(entry.getValue().getValue());
            heroDamageList.add(heroDamage);
        }
        return  heroDamageList;
    }
}
