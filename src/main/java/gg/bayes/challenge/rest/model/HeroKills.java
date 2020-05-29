package gg.bayes.challenge.rest.model;

import lombok.Data;

@Data
public class HeroKills {

    private String hero;

    private Integer kills;

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }
}
