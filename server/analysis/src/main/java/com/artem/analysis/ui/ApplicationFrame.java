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
    private TrialTreePanel treePanel;

    @Autowired
    private TrialDetailsPanel detailsPanel;

    public ApplicationFrame() throws HeadlessException {
        super("Analysis application");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JPanel container = new JPanel(new BorderLayout(2, 2));
        container.add(treePanel, BorderLayout.WEST);
        container.add(detailsPanel, BorderLayout.CENTER);
        add(container);
        pack();
        setVisible(true);
    }
}
