package gg.bayes.challenge.rest.model;

import lombok.Data;

@Data
public class HeroSpells {

    private String spell;

    private Integer casts;

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public Integer getCasts() {
        return casts;
    }

    public void setCasts(Integer casts) {
        this.casts = casts;
    }
}
