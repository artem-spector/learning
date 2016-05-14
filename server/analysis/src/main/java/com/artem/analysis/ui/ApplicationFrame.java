package com.artem.analysis.ui;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/13/16
 */
@Component
public class ApplicationFrame extends JFrame implements InitializingBean {

    @Autowired
    private TrialBrowsingPanel form;

    public ApplicationFrame() throws HeadlessException {
        super("Analysis application");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        add(form);
        pack();
        setVisible(true);
    }
}
