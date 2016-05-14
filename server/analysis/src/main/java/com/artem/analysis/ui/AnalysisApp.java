package com.artem.analysis.ui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/13/16
 */
@SpringBootApplication(scanBasePackages = {"com.artem"})
public class AnalysisApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AnalysisApp.class).web(false).headless(false).run(args);
    }

    public JFrame createFrame() {
        JFrame frame = new JFrame("Analysis app");
        return frame;
    }
}
