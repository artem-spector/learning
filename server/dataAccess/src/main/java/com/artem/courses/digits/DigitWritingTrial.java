package com.artem.courses.digits;

import com.artem.learning.server.model.Trial;
import com.artem.server.api.drawing.DrawingRawData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Document!
 *
 * @author artem on 3/5/16.
 */
public class DigitWritingTrial extends Trial {

    @JsonIgnore
    private static ObjectMapper mapper = new ObjectMapper();

    @JsonProperty("write_digit")
    private String digit;

    @JsonProperty("drawing")
    private DrawingRawData drawing;

    public DigitWritingTrial() {
    }

    public DigitWritingTrial(int digit) {
        this.digit = digitName(digit);
    }

    private String digitName(int digit) {
        switch (digit) {
            case 0: return "zero";
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            default:
                throw new RuntimeException("Invalid digit: " + digit);
        }
    }

    @Override
    public Object getTask() {
        Map<String, Object> challenge = new HashMap<>();
        challenge.put("stimulus", "Write " + digit);
        return challenge;
    }

    @Override
    protected Object setResponseAndGetFeedback(String responseStr) {
        try {
            drawing = mapper.readValue(responseStr, DrawingRawData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "good";
    }
}
