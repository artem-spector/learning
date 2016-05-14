package com.artem.analysis.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/14/16
 */
@Component
public class TrialDetailsPanel extends JPanel implements TrialTreeSelectionListener {

    public TrialDetailsPanel() {
        super();
        setPreferredSize(new Dimension(300, 500));
    }

    @Override
    public void nodeSelected(Object node) {
        removeAll();
        if (node != null && (node instanceof TrialTreeNode))
            ((TrialTreeNode) node).buildDetailsPanel(this);
        revalidate();
        repaint();
    }
}
