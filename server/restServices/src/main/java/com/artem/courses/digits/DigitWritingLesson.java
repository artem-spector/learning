package com.artem.courses.digits;

import com.artem.learning.server.model.Lesson;
import com.artem.learning.server.model.Trial;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Document!
 *
 * @author artem on 3/5/16.
 */
public class DigitWritingLesson extends Lesson {

    @JsonProperty
    private int plannedNumTrials;

    public DigitWritingLesson() {
    }

    public DigitWritingLesson(int plannedNumTrials) {
        this.plannedNumTrials = plannedNumTrials;
    }

    @Override
    public boolean hasNextTrial() {
        if (getNumTrials() >= plannedNumTrials) {
            end();
        }
        return getEndTime() == null;
    }

    @Override
    public Trial generateTrial() {
        return new DigitWritingTrial();
    }
}
