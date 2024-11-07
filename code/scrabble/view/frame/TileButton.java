package scrabble.view.frame;

import scrabble.model.TileScore;

import javax.swing.*;
import java.awt.*;

public class TileButton extends JButton {
    private final TileScore letterScore;
    private final Font scoreFont;

    public TileButton(TileScore letterScore) {
        super(letterScore.name());
        this.letterScore = letterScore;
        scoreFont = getFont().deriveFont(8f);
        setFont(getFont().deriveFont(Font.BOLD, 12f));
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.paintComponent(g);

        g2.setPaint(Color.BLACK);
        g2.setFont(scoreFont);
        g2.drawString(letterScore.getScore() + "", getWidth() / 2 + 8,getHeight() / 2 + 8);
        g2.dispose();
    }
}
