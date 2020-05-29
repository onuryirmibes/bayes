package gg.bayes.challenge.db.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class HeroDamageEvent {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private String dealerHero;

    private String targetHero;

    @Enumerated(EnumType.STRING)
    private DamageType type;

    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Match match;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDealerHero() {
        return dealerHero;
    }

    public void setDealerHero(String dealerHero) {
        this.dealerHero = dealerHero;
    }

    public String getTargetHero() {
        return targetHero;
    }

    public void setTargetHero(String targetHero) {
        this.targetHero = targetHero;
    }

    public DamageType getType() {
        return type;
    }

    public void setType(DamageType type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
