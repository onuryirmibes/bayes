package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.db.entity.*;
import gg.bayes.challenge.db.repository.*;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.util.MatchServiceUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PurchasedItemRepository itemRepository;

    @Mock
    private CastedSpellRepository spellRepository;

    @Mock
    private HeroDamageEventRepository damageEventRepository;

    @Mock
    private HeroKillEventRepository killEventRepository;

    @InjectMocks
    private MatchServiceImpl service;

    @Test
    public void shouldIngestMatchForHeroItem() {
        mockCreateMatch();
        final String lineWithBuyItem = "[00:27:17.088] npc_dota_hero_bane buys item item_bracer";
        final Long matchId = service.ingestMatch(lineWithBuyItem);
        final ArgumentCaptor<PurchasedItem> itemCaptor = ArgumentCaptor.forClass(PurchasedItem.class);
        verify(itemRepository).save(itemCaptor.capture());

        final PurchasedItem purchasedItem = itemCaptor.getValue();
        final long timeStamp = MatchServiceUtil.convertTimeStamp("00:27:17.088");
        assertThat(purchasedItem.getTimeStamp()).isEqualTo(timeStamp);
        assertThat(purchasedItem.getHero()).isEqualTo("bane");
        assertThat(purchasedItem.getItemName()).isEqualTo("bracer");
        assertThat(purchasedItem.getMatch().getId()).isEqualTo(matchId);
    }

    @Test
    public void shouldIngestMatchForHeroSpell() {
        mockCreateMatch();
        final String lineWithCastedSpell
                = "[00:28:04.710] npc_dota_hero_rubick casts ability rubick_fade_bolt (lvl 4) on npc_dota_hero_puck";
        final Long matchId = service.ingestMatch(lineWithCastedSpell);
        final ArgumentCaptor<CastedSpell> spellCaptor = ArgumentCaptor.forClass(CastedSpell.class);
        verify(spellRepository).save(spellCaptor.capture());

        final CastedSpell spell = spellCaptor.getValue();
        assertThat(spell.getHero()).isEqualTo("rubick");
        assertThat(spell.getSpellName()).isEqualTo("rubick_fade_bolt");
        assertThat(spell.getSpellLevel()).isEqualTo(4);
        assertThat(spell.getMatch().getId()).isEqualTo(matchId);
    }

    @Test
    public void shouldIngestMatchForHeroDamageWithHit() {
        mockCreateMatch();
        final String lineWithHitDamage
                = "[00:28:03.510] npc_dota_hero_bane hits npc_dota_hero_puck" +
                " with dota_unknown for 12 damage (1099->1087)";
        final Long matchId = service.ingestMatch(lineWithHitDamage);
        final ArgumentCaptor<HeroDamageEvent> damageCaptor = ArgumentCaptor.forClass(HeroDamageEvent.class);
        verify(damageEventRepository).save(damageCaptor.capture());

        final HeroDamageEvent damage = damageCaptor.getValue();
        assertThat(damage.getDealerHero()).isEqualTo("bane");
        assertThat(damage.getTargetHero()).isEqualTo("puck");
        assertThat(damage.getType()).isEqualTo(DamageType.HIT);
        assertThat(damage.getValue()).isEqualTo(12);
        assertThat(damage.getMatch().getId()).isEqualTo(matchId);
    }

    @Test
    public void shouldIngestMatchForHeroDamageWithSpell() {
        mockCreateMatch();
        final String lineWithSpellDamage
                = "[00:28:03.277] npc_dota_hero_snapfire hits npc_dota_hero_mars" +
                " with snapfire_spit_creep for 43 damage (776->733)";
        final Long matchId = service.ingestMatch(lineWithSpellDamage);
        final ArgumentCaptor<HeroDamageEvent> damageCaptor = ArgumentCaptor.forClass(HeroDamageEvent.class);
        verify(damageEventRepository).save(damageCaptor.capture());

        final HeroDamageEvent damage = damageCaptor.getValue();
        assertThat(damage.getDealerHero()).isEqualTo("snapfire");
        assertThat(damage.getTargetHero()).isEqualTo("mars");
        assertThat(damage.getType()).isEqualTo(DamageType.SPELL);
        assertThat(damage.getValue()).isEqualTo(43);
        assertThat(damage.getMatch().getId()).isEqualTo(matchId);
    }

    @Test
    public void shouldIngestMatchForHeroDamageWithItem() {
        mockCreateMatch();
        final String lineWithItemDamage
                = "[00:28:35.969] npc_dota_hero_bloodseeker hits npc_dota_hero_pangolier" +
                " with item_dragon_scale for 11 damage (688->677)";
        final Long matchId = service.ingestMatch(lineWithItemDamage);
        final ArgumentCaptor<HeroDamageEvent> damageCaptor = ArgumentCaptor.forClass(HeroDamageEvent.class);
        verify(damageEventRepository).save(damageCaptor.capture());

        final HeroDamageEvent damage = damageCaptor.getValue();
        assertThat(damage.getDealerHero()).isEqualTo("bloodseeker");
        assertThat(damage.getTargetHero()).isEqualTo("pangolier");
        assertThat(damage.getType()).isEqualTo(DamageType.ITEM);
        assertThat(damage.getValue()).isEqualTo(11);
        assertThat(damage.getMatch().getId()).isEqualTo(matchId);
    }

    @Test
    public void shouldIngestMatchForHeroKill() {
        mockCreateMatch();
        final String lineWithHeroKill
                = "[00:28:38.135] npc_dota_hero_pangolier is killed by npc_dota_hero_bloodseeker";
        final Long matchId = service.ingestMatch(lineWithHeroKill);
        final ArgumentCaptor<HeroKillEvent> killCaptor = ArgumentCaptor.forClass(HeroKillEvent.class);
        verify(killEventRepository).save(killCaptor.capture());

        final HeroKillEvent kill = killCaptor.getValue();
        assertThat(kill.getKillerHero()).isEqualTo("bloodseeker");
        assertThat(kill.getTargetHero()).isEqualTo("pangolier");
        assertThat(kill.getMatch().getId()).isEqualTo(matchId);
    }

    @Test
    public void shouldIgnoreUnrelatedLinesForIngestMatch() {
        mockCreateMatch();
        final String unrelatedLines = "[00:00:04.999] game state is now 2\n" +
                "[00:28:34.969] npc_dota_neutral_satyr_trickster is killed by npc_dota_hero_dragon_knight";
        service.ingestMatch(unrelatedLines);
        verify(itemRepository, never()).save(ArgumentMatchers.any(PurchasedItem.class));
        verify(spellRepository, never()).save(ArgumentMatchers.any(CastedSpell.class));
        verify(damageEventRepository, never()).save(ArgumentMatchers.any(HeroDamageEvent.class));
        verify(killEventRepository, never()).save(ArgumentMatchers.any(HeroKillEvent.class));
    }

    private Match mockCreateMatch() {
        final Match match = new Match();
        match.setId(1L);
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        return match;
    }

    private Match mockFindMatch(final Long matchId) {
        final Match match = new Match();
        match.setId(matchId);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        return match;
    }

    @Test
    public void shouldGetHeroKills() {
        final Long matchId = 1L;
        final Match match = mockFindMatch(matchId);
        final List<HeroKillEvent> killEvents = new ArrayList<>();
        killEvents.add(generateKillEvent("bane", "puck"));
        killEvents.add(generateKillEvent("bane", "puck"));
        killEvents.add(generateKillEvent("bane", "puck"));

        when(killEventRepository.findByMatch(match)).thenReturn(killEvents);
        final List<HeroKills> heroKillsList = service.getHeroKills(matchId);

        assertThat(heroKillsList.size()).isEqualTo(1);
        final HeroKills heroKills = heroKillsList.iterator().next();
        assertThat(heroKills.getHero()).isEqualTo("bane");
        assertThat(heroKills.getKills()).isEqualTo(3);
    }

    private HeroKillEvent generateKillEvent(final String killer, final String target) {
        final HeroKillEvent heroKillEvent = new HeroKillEvent();
        heroKillEvent.setKillerHero(killer);
        heroKillEvent.setTargetHero(target);
        return heroKillEvent;
    }

    @Test
    public void shouldGetHeroItems() {
        final Long matchId = 1L;
        final Match match = mockFindMatch(matchId);
        final List<PurchasedItem> purchasedItems = new ArrayList<>();
        purchasedItems.add(generateItem("tango", 10000));
        purchasedItems.add(generateItem("bracer", 10001));
        purchasedItems.add(generateItem("monkey_king_bar", 10002));
        final String heroName = "bloodseeker";

        when(itemRepository.findByMatchAndHero(match, heroName)).thenReturn(purchasedItems);
        final List<HeroItems> heroItemsList = service.getHeroItems(matchId, heroName);

        assertThat(heroItemsList.stream().map(item -> item.getItem()))
                .containsExactlyInAnyOrder("tango", "bracer", "monkey_king_bar");
        assertThat(heroItemsList.stream().map(item -> item.getTimestamp()))
                .containsExactlyInAnyOrder(10000L, 10001L, 10002L);
    }

    private PurchasedItem generateItem(final String item, final long timeStamp) {
        final PurchasedItem purchasedItem = new PurchasedItem();
        purchasedItem.setItemName(item);
        purchasedItem.setTimeStamp(timeStamp);
        return purchasedItem;
    }

    @Test
    public void shouldGetHeroSpells() {
        final Long matchId = 1L;
        final Match match = mockFindMatch(matchId);
        final List<CastedSpell> castedSpells = new ArrayList<>();
        castedSpells.add(generateSpell("spell_a"));
        castedSpells.add(generateSpell("spell_a"));
        castedSpells.add(generateSpell("spell_b"));
        final String heroName = "bloodseeker";

        when(spellRepository.findByMatchAndHero(match, heroName)).thenReturn(castedSpells);
        final List<HeroSpells> heroSpellsList = service.getHeroSpells(matchId, heroName);

        assertThat(heroSpellsList.stream().map(spell -> spell.getSpell()))
                .containsExactlyInAnyOrder("spell_a", "spell_b");
        assertThat(heroSpellsList.stream().map(spell -> spell.getCasts()))
                .containsExactlyInAnyOrder(1, 2);
    }

    private CastedSpell generateSpell(final String spellName) {
        final CastedSpell castedSpell = new CastedSpell();
        castedSpell.setSpellName(spellName);
        return castedSpell;
    }

    @Test
    public void shouldGetHeroDamage() {
        final Long matchId = 1L;
        final Match match = mockFindMatch(matchId);
        final List<HeroDamageEvent> damageEvents = new ArrayList<>();
        damageEvents.add(generateDamage("bane", 10));
        damageEvents.add(generateDamage("bane", 20));
        damageEvents.add(generateDamage("bane", 30));
        final String heroName = "bloodseeker";

        when(damageEventRepository.findByMatchAndDealerHero(match, heroName)).thenReturn(damageEvents);
        final List<HeroDamage> heroDamageList = service.getHeroDamage(matchId, heroName);

        assertThat(heroDamageList.size()).isEqualTo(1);
        final HeroDamage heroDamage = heroDamageList.iterator().next();
        assertThat(heroDamage.getTarget()).isEqualTo("bane");
        assertThat(heroDamage.getDamageInstances()).isEqualTo(3);
        assertThat(heroDamage.getTotalDamage()).isEqualTo(60);
    }

    private HeroDamageEvent generateDamage(final String targetHero, final int value) {
        final HeroDamageEvent damage = new HeroDamageEvent();
        damage.setTargetHero(targetHero);
        damage.setValue(value);
        return damage;
    }

}
