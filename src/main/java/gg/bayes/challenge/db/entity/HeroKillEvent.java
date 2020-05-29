package gg.bayes.challenge.db.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class HeroKillEvent {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private String killerHero;

    private String targetHero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Match match;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getKillerHero() {
        return killerHero;
    }

    public void setKillerHero(String killerHero) {
        this.killerHero = killerHero;
    }

    public String getTargetHero() {
        return targetHero;
    }

    public void setTargetHero(String targetHero) {
        this.targetHero = targetHero;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
