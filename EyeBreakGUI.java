import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class EyeBreakGUI {
    private JButton startButton, stopButton;
    private JLabel instructionLabel;
    private JProgressBar progressBar;
    private boolean running = false;

    public EyeBreakGUI() {
        createGUI();
    }

    private void createGUI() {
        JFrame frame = new JFrame("Eye Break Program");
        frame.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        instructionLabel = new JLabel("Click start to begin the eye break program");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(instructionLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    running = true;
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    instructionLabel.setText("Running!");
                    startEyeBreakProgram();
                }
            }
        });
        panel.add(startButton);

        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    running = false;
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    instructionLabel.setText("Click start to begin the eye break program");
                    progressBar.setValue(0);
                }
            }
        });
        panel.add(stopButton);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        frame.add(progressBar, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void startEyeBreakProgram() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (running) {
                        for (int i = 0; i <= 100 && running; i++) {
                            progressBar.setValue(i);
                            Thread.sleep(1200*1000); // 20 minutes
                        }
                        if (running) {
                            String message = "Time to take an eye break! Look away from the screen for 10-20 seconds.";
                            String title = "Eye Break";
                            int messageType = JOptionPane.INFORMATION_MESSAGE;
                            JOptionPane.showMessageDialog(null, message, title, messageType);
                            progressBar.setValue(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    running = false;
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    instructionLabel.setText("Click start to begin the eye break program");
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EyeBreakGUI();
            }
        });
    }
}
