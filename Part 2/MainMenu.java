import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


/**
 * The Main Menu UI , executed at the start of application
 * @author ashar
 * @version 1.1
 */

public class MainMenu {
    /**
     * Show the main menu interface
     */
    public static void showUI() {
        JFrame MainMenu = new JFrame("HorseRacer23");
        MainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainMenu.setSize(900, 400);
        MainMenu.setLocationRelativeTo(null);


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
        ImageIcon logoIcon = new ImageIcon("Images/logo.png");
        JLabel titleLogo = new JLabel(logoIcon);
        titleLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Image originalImage = logoIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(400, -1, Image.SCALE_SMOOTH); // 300px width, maintain aspect ratio
        titleLogo.setIcon(new ImageIcon(scaledImage));

        JLabel caption = new JLabel("Welcome to horse racer! Press \"Start\" to begin.");
        caption.setFont(new Font("Arial", Font.PLAIN, 12));
        caption.setAlignmentX(Component.CENTER_ALIGNMENT);
        caption.setForeground(Color.WHITE);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLogo);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        centerPanel.add(caption);
        centerPanel.add(Box.createVerticalGlue());
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

        //JMENU
        JMenuBar MenuBar = new JMenuBar();
        JMenu History = new JMenu("History");
        JMenuItem ViewHistory = new JMenuItem("Race");
        JMenuItem UserHistory = new JMenuItem("User");

        UserHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.ShowUser();
            }
        });

        ViewHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SaveSystem.Fetch();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        History.add(UserHistory);
        History.add(ViewHistory);
        MenuBar.add(History);

        MainMenu.setJMenuBar(MenuBar);


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