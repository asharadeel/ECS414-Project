import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GlobalStyler {

    /**
     * Apply global styles to UI
     */
    public static void applyGlobalStyle() {
        try {
            UIManager.put("Button.background", Color.BLACK);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 15, 5, 15));
            UIManager.put("Button.focusPainted", false);
            UIManager.put("ButtonUI", RoundedButtonUI.class.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE ALL BUTTONS TO BE ROUNDED AND HAVE A NEW GRAPHICAL LOOK.
     */
    public static class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            JButton button = (JButton) c;

            g2.setColor(button.getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, button.getWidth(), button.getHeight(), 20, 20));

            super.paint(g2, button);
            g2.dispose();
        }
    }



}