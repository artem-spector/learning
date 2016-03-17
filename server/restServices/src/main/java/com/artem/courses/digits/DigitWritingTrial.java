package com.artem.courses.digits;

import com.artem.learning.server.model.Trial;
import com.artem.server.api.drawing.DrawingRawData;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Document!
 *
 * @author artem on 3/5/16.
 */
public class DigitWritingTrial extends Trial {

    @JsonProperty("write_digit")
    private String digit;

    @JsonProperty("drawing")
    private DrawingRawData drawing;

    @Override
    public Object getTask() {
        Map<String, Object> challenge = new HashMap<>();
        challenge.put("stimulus", "Write " + digit);
        return challenge;
    }

    @Override
    protected Object setResponseAndGetFeedback(Object response) {
        drawing = (DrawingRawData) response;
        return "good";
    }
}
