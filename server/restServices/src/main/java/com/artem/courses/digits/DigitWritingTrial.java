package com.artem.courses.digits;

import com.artem.learning.server.model.Trial;
import com.artem.server.api.drawing.DrawingRawData;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public Object getStimulus() {
        return "Write " + digit;
    }

    @Override
    public Object getHelp() {
        return null;
    }

    @Override
    protected void setResponse(Object response) {
        drawing = (DrawingRawData) response;
    }
}
