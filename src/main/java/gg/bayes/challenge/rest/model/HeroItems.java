package gg.bayes.challenge.rest.model;

import lombok.Data;

@Data
public class HeroItems {

    private String item;

    private Long timestamp;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
