package gg.bayes.challenge.service.util;

import gg.bayes.challenge.db.entity.DamageType;
import gg.bayes.challenge.exception.DotaChallangeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class MatchServiceUtil {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");

    public static long convertTimeStamp(final String timeStampToken) {
        try {
            SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
            return SDF.parse(timeStampToken).getTime();
        } catch (final ParseException e) {
            throw new DotaChallangeException("Failed to parse time stamp.", e);
        }
    }

    public static DamageType getDamageType(final String damageType) {
        if ("dota_unknown".equals(damageType)) {
            return DamageType.HIT;
        }
        if (damageType.startsWith("item_")) {
            return DamageType.ITEM;
        }
        return DamageType.SPELL;
    }

    public static String trimHeroName(final String heroName) {
        return heroName.replace("npc_dota_hero_", "");
    }
}
