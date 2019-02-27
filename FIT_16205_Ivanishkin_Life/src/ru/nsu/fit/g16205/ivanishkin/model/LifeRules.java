package ru.nsu.fit.g16205.ivanishkin.model;

import ru.nsu.fit.g16205.ivanishkin.utils.Utils;

public class LifeRules {
    public static class LifeRulesBuilder {
        public static final double FST_IMPACT_DEFAULT = 1.0;
        public static final double SND_IMPACT_DEFAULT = 0.3;
        public static final double LIVE_BEGIN_DEFAULT = 2.0;
        public static final double LIVE_END_DEFAULT = 3.3;
        public static final double BIRTH_BEGIN_DEFAULT = 2.3;
        public static final double BIRTH_END_DEFAULT = 2.9;

        private Double FST_IMPACT;
        private Double SND_IMPACT;
        private Double LIVE_BEGIN;
        private Double LIVE_END;
        private Double BIRTH_BEGIN;
        private Double BIRTH_END;

        public LifeRulesBuilder withLiveEnd(double val) {
            this.LIVE_END = val;
            return this;
        }

        public LifeRulesBuilder withBirthBegin(double val) {
            this.BIRTH_BEGIN = val;
            return this;
        }

        public LifeRulesBuilder withBirthEnd(double val) {
            this.BIRTH_END = val;
            return this;
        }

        public LifeRulesBuilder withFstImpact(double val) {
            FST_IMPACT = val;
            return this;
        }

        public LifeRulesBuilder withSndImpact(double val) {
            this.SND_IMPACT = val;
            return this;
        }

        public LifeRulesBuilder withLiveBegin(double val) {
            this.LIVE_BEGIN = val;
            return this;
        }

        public LifeRules build() {
            return new LifeRules(
                    Utils.notNullOrElse(FST_IMPACT, FST_IMPACT_DEFAULT),
                    Utils.notNullOrElse(SND_IMPACT, SND_IMPACT_DEFAULT),
                    Utils.notNullOrElse(LIVE_BEGIN, LIVE_BEGIN_DEFAULT),
                    Utils.notNullOrElse(LIVE_END, LIVE_END_DEFAULT),
                    Utils.notNullOrElse(BIRTH_BEGIN, BIRTH_BEGIN_DEFAULT),
                    Utils.notNullOrElse(BIRTH_END, BIRTH_END_DEFAULT));
        }
    }

    public static final LifeRules DEFAULT = new LifeRulesBuilder().build();

    public final double FST_IMPACT;
    public final double SND_IMPACT;
    public final double LIFE_BEGIN;
    public final double LIFE_END;
    public final double BIRTH_BEGIN;
    public final double BIRTH_END;

    public LifeRules(double fstImpact,
                     double sndImpact,
                     double lifeBegin,
                     double lifeEnd,
                     double birthBegin,
                     double birthEnd) {
        if (!(lifeBegin <= birthBegin && birthBegin <= birthEnd && birthEnd <= lifeEnd)) {
            throw new IllegalArgumentException("Parameters should satisfy lifeBegin <= birthBegin <= birthEnd <= lifeEnd");
        }
        FST_IMPACT = fstImpact;
        SND_IMPACT = sndImpact;
        LIFE_BEGIN = lifeBegin;
        LIFE_END = lifeEnd;
        BIRTH_BEGIN = birthBegin;
        BIRTH_END = birthEnd;
    }
}
