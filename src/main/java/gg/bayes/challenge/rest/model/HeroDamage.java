package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HeroDamage {

    private String target;

    @JsonProperty("damage_instances")
    private Integer damageInstances;

    @JsonProperty("total_damage")
    private Integer totalDamage;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getDamageInstances() {
        return damageInstances;
    }

    public void setDamageInstances(Integer damageInstances) {
        this.damageInstances = damageInstances;
    }

    public Integer getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(Integer totalDamage) {
        this.totalDamage = totalDamage;
    }
}
