import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu {

    public static void showUI() {
        JFrame MainMenu = new JFrame("HorseRacer");
        MainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainMenu.setSize(500, 500);

        // Create a background panel with image
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("images/mainMenuBackground.jpg").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        MainMenu.setContentPane(backgroundPanel);

        // CENTER Panel - for title and caption
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Main Title
        JLabel Title = new JLabel("HorseRacer I");
        Title.setFont(new Font("Arial", Font.BOLD, 24));
        Title.setAlignmentX(Component.CENTER_ALIGNMENT);
        Title.setForeground(Color.WHITE);

        // Caption
        JLabel caption = new JLabel("Welcome to horse racer! Press \"Start\" to begin.");
        caption.setFont(new Font("Arial", Font.PLAIN, 12));
        caption.setAlignmentX(Component.CENTER_ALIGNMENT);
        caption.setForeground(Color.WHITE);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(Title);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        centerPanel.add(caption);
        centerPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel - for buttons
        JPanel BottomSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        BottomSection.setOpaque(false);
        BottomSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton StartRacer = new JButton("Start Racer");
        StartRacer.setPreferredSize(new Dimension(150, 40));
        StartRacer.setBackground(Color.BLACK);
        StartRacer.setForeground(Color.WHITE);

        JButton CloseMM = new JButton("Close");
        CloseMM.setPreferredSize(new Dimension(150, 40));
        CloseMM.setBackground(Color.WHITE);
        CloseMM.setForeground(Color.BLACK);

        // Add action listeners
        StartRacer.addActionListener(e -> {
            MainMenu.setVisible(false);
            GetRaceInfoUI.showUI(); // Open race creation UI
        });

        CloseMM.addActionListener(e -> System.exit(0));

        BottomSection.add(StartRacer);
        BottomSection.add(CloseMM);
        backgroundPanel.add(BottomSection, BorderLayout.SOUTH);

        MainMenu.setVisible(true);
    }
}