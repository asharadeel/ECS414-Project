import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.*;

public class ViewStatistics {
    private GameStatistics stats;
    private JFrame window;
    private JPanel panel;

    private JComboBox<String> horseDropdown;

    ViewStatistics(GameStatistics stats) {
        this.stats = stats;
    }

    public void showUI() {
        window = new JFrame("Statistical Review");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(900, 400);
        window.setLocationRelativeTo(null);

        // Create the panel and set layout to BorderLayout
        panel = new JPanel(){
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon("images/stats.jpg").getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    int imgWidth = backgroundImage.getWidth(this);
                    int imgHeight = backgroundImage.getHeight(this);

                    int tilesX = (int) Math.ceil((double) getWidth() / imgWidth);
                    int tilesY = (int) Math.ceil((double) getHeight() / imgHeight);

                    for (int y = 0; y < tilesY; y++) {
                        for (int x = 0; x < tilesX; x++) {
                            g.drawImage(backgroundImage, x * imgWidth, y * imgHeight, this);
                        }
                    }

                    g.setColor(new Color(255, 255, 255, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create the title label and set font size
        JLabel title = new JLabel("Statistical Review", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));  // Set title font size to 32
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // Center title horizontally

        // Create the caption label and set font size
        JLabel caption = new JLabel("Press the View option to select table", SwingConstants.CENTER);
        caption.setFont(new Font("Arial", Font.PLAIN, 20));  // Set caption font size to 20
        caption.setAlignmentX(Component.CENTER_ALIGNMENT); // Center caption horizontally

        // Add title and caption to the panel
        panel.add(title);
        panel.add(caption);

        // Center the content vertically
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(50));

        window.add(panel);


        JMenuBar menuBar = new JMenuBar();
        JMenu viewOptions = new JMenu("View");

        JMenuItem viewDvT = new JMenuItem("Distance vs Time");
        viewDvT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenDistanceVTime();
            }
        });

        JMenuItem viewSvT = new JMenuItem("Average Speed vs Time");
        viewSvT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenSpeedVTime();
            }
        });


        viewOptions.add(viewDvT);
        viewOptions.add(viewSvT);

        menuBar.add(viewOptions);
        window.setJMenuBar(menuBar);


        window.setVisible(true);
    }

    private void OpenDistanceVTime() {
        // Get the horse data
        HorseData[] horses = stats.getHorses();
        if (horses == null || horses.length == 0) return;

        // Create the chart
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(300)
                .title("Horse Distance vs Time")
                .xAxisTitle("Distance Travelled (m)")
                .yAxisTitle("Time (s)")
                .build();

        // Set manual axis bounds
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax((double) stats.getRaceLength());
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax((double) stats.getTime());

        for (HorseData horse : horses) {
            List<Integer> xData = Arrays.asList(0, horse.getDistanceTravelled());
            List<Double> yData = Arrays.asList(0.0, horse.getTimeTaken());

            XYSeries series = chart.addSeries(horse.getName(), xData, yData);
            series.setMarker(SeriesMarkers.CIRCLE);  // Circle at each point
            series.setLineWidth(2.0f);  // Thicker line
        }


        panel.removeAll();
        panel.add(new XChartPanel<>(chart));
        panel.revalidate();
        panel.repaint();
    }

    private void OpenSpeedVTime() {
        // Get the horse data
        HorseData[] horses = stats.getHorses();
        if (horses == null || horses.length == 0) return;

        // Create the chart
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(300)
                .title("Horse Speed vs Time")
                .xAxisTitle("Average Speed (km/h)")
                .yAxisTitle("Time (s)")
                .build();

        // Set manual axis bounds
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax(50.0);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax((double) stats.getTime());

        for (HorseData horse : horses) {
            List<Double> xData = Arrays.asList(0.0, horse.getAverageSpeed());
            List<Double> yData = Arrays.asList(0.0, horse.getTimeTaken());

            XYSeries series = chart.addSeries(horse.getName(), xData, yData);
            series.setMarker(SeriesMarkers.CIRCLE);
            series.setLineWidth(2.0f);
        }


        panel.removeAll();
        panel.add(new XChartPanel<>(chart));
        panel.revalidate();
        panel.repaint();
    }


}
