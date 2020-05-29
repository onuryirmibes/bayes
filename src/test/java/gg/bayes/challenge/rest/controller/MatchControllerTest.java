package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MatchController.class)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService service;

    @Test
    public void shouldIngestMatch() throws Exception {
        final String payload = "test_payload";
        Mockito.when(service.ingestMatch(payload)).thenReturn(1L);
        mockMvc.perform(post("/api/match").contentType(MediaType.TEXT_PLAIN).content(payload))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void shouldGetHeroKills() throws Exception {
        final HeroKills heroKills = new HeroKills();
        final long matchId = 1L;
        Mockito.when(service.getHeroKills(matchId)).thenReturn(List.of(heroKills));
        mockMvc.perform(get("/api/match/" + matchId))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void shouldGetHeroItems() throws Exception {
        final HeroItems heroItems = new HeroItems();
        final long matchId = 1L;
        final String heroName = "hero_name";
        Mockito.when(service.getHeroItems(matchId, heroName)).thenReturn(List.of(heroItems));
        mockMvc.perform(get("/api/match/" + matchId + "/" + heroName + "/items"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void shouldGetHeroSpells() throws Exception {
        final HeroSpells heroSpells = new HeroSpells();
        final long matchId = 1L;
        final String heroName = "hero_name";
        Mockito.when(service.getHeroSpells(matchId, heroName)).thenReturn(List.of(heroSpells));
        mockMvc.perform(get("/api/match/" + matchId + "/" + heroName + "/spells"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    public void shouldGetHeroDamage() throws Exception {
        final HeroDamage heroDamage = new HeroDamage();
        final long matchId = 1L;
        final String heroName = "hero_name";
        Mockito.when(service.getHeroDamage(matchId, heroName)).thenReturn(List.of(heroDamage));
        mockMvc.perform(get("/api/match/" + matchId + "/" + heroName + "/damage"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }
}
