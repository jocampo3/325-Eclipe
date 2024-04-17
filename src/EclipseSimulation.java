import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EclipseSimulation extends JPanel {
    private CelestialBody sun;
    private CelestialBody earth;
    private CelestialBody moon;

    private int day = 1;
    private boolean isPlaying = false;
    private Timer timer;

    private JLabel dayLabel;

    public EclipseSimulation() {
        // Initialize celestial bodies
        sun = new CelestialBody("Sun", 350, 250, Color.YELLOW, 50);
        earth = new CelestialBody("Earth", 150, 250, Color.BLUE, 30);
        moon = new CelestialBody("Moon", 250, 250, Color.GRAY, 20);

        // Set the Earth as the center of orbit for the Moon
        moon.setOrbitCenter(earth);

        // Set up timer for animation
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    moveCelestialBodies();
                    repaint();
                }
            }
        });

        // Create controls panel
        JPanel controlsPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        JButton stepForwardButton = new JButton("Step Forward");
        JButton stepBackwardButton = new JButton("Step Backward");
        dayLabel = new JLabel("Day: " + day);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPlaying = true;
                timer.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPlaying = false;
                timer.stop();
            }
        });

        stepForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepForward();
                repaint();
            }
        });

        stepBackwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepBackward();
                repaint();
            }
        });

        controlsPanel.add(playButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(stepForwardButton);
        controlsPanel.add(stepBackwardButton);
        controlsPanel.add(dayLabel);

        // Add controls panel to main panel
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.SOUTH);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);

        // Draw celestial bodies
        sun.draw(g);
        earth.draw(g);
        moon.draw(g);
    }

    private void moveCelestialBodies() {
        earth.moveAroundSun();
        moon.moveAroundEarth();

        dayLabel.setText("Day: " + day); // Update day label

        // Increment day counter
        if (++day > 365) {
            day = 1;
        }
    }

    private void stepForward() {
        moveCelestialBodies();
    }

    private void stepBackward() {
        // Decrease day counter
        if (--day < 1) {
            day = 365;
        }

        // Since we don't have backward animation, we'll reset the simulation
        sun.resetPosition();
        earth.resetPosition();
        moon.resetPosition();
        dayLabel.setText("Day: " + day); // Update day label
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Solar Eclipse Simulation");
        EclipseSimulation panel = new EclipseSimulation();
        frame.add(panel);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class CelestialBody {
    private String name;
    private int x;
    private int y;
    private Color color;
    private int radius;
    private CelestialBody orbitCenter;
    private double angle;

    public CelestialBody(String name, int x, int y, Color color, int radius) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
        this.angle = 0; // Initial angle
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - radius / 2, y - radius / 2, radius, radius);
    }

    public void moveAroundSun() {
        // Simulate rotation around the Sun
        angle += Math.toRadians(1); // Increment angle for rotation speed around the Sun
        if (angle >= 2 * Math.PI) {
            angle = 0; // Reset angle after completing one full revolution
        }
        int rotationRadius = 200; // Adjust the radius for rotation around the Sun
        int sunX = 350; // X coordinate of the Sun
        int sunY = 250; // Y coordinate of the Sun
        x = sunX + (int) (rotationRadius * Math.cos(angle));
        y = sunY + (int) (rotationRadius * Math.sin(angle));
    }

    public void moveAroundEarth() {
        // Simulate orbit around the Earth
        angle += Math.toRadians(200); // Increment angle for orbit speed around the Earth
        int orbitRadius = 80; // Adjust the radius for orbit around the Earth
        int earthX = orbitCenter.getX(); // X coordinate of the Earth
        int earthY = orbitCenter.getY(); // Y coordinate of the Earth
        x = earthX + (int) (orbitRadius * Math.cos(angle));
        y = earthY + (int) (orbitRadius * Math.sin(angle));
    }

    public void setOrbitCenter(CelestialBody orbitCenter) {
        this.orbitCenter = orbitCenter;
    }

    public void resetPosition() {
        // Reset position to initial state
        angle = 0;
        x = orbitCenter.getX();
        y = orbitCenter.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
}
