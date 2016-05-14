package com.artem.analysis.ui;

import com.artem.server.api.drawing.DrawingRawData;
import com.artem.server.api.drawing.MotionData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/14/16
 */
public class DrawingPlaybackPanel extends JPanel {

    private final MotionData[] motions;
    private Path2D path;

    public DrawingPlaybackPanel(DrawingRawData drawing) {
        motions = drawing.getMotions(0);
        setSize(calculateSize());
    }

    private Dimension calculateSize() {
        float maxX = 0;
        float maxY = 0;
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        for (MotionData motion : motions) {
            maxX = Math.max(maxX, motion.x);
            maxY = Math.max(maxY, motion.y);
            minX = Math.min(minX, motion.x);
            minY = Math.min(minY, motion.y);
        }

        for (MotionData motion : motions) {
            motion.x -= minX;
            motion.y -= minY;
        }

        return new Dimension((int) (maxX - minX + 1), (int) (maxY - minY) + 1);
    }

    public void clear() {
        Graphics2D graphics = (Graphics2D) getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    protected void play() {
        path = new Path2D.Float();
        Graphics2D g2 = (Graphics2D) getGraphics();
        for (MotionData motion : motions) {
            try {
                Thread.currentThread().sleep(motion.time);
                switch (motion.motion) {
                    case Down:
                        path.moveTo(motion.x, motion.y);
                        break;
                    case Move:
                        path.lineTo(motion.x, motion.y);
                        break;
                    default:
                        // do nothing
                }
                g2.draw(path);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
