import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 * It creates a GUI with a start and stop button, a progress bar, and a label. When the start button is pressed, the
 * progress bar starts filling up. When the progress bar is full, a dialog box pops up telling the user to take a break
 *
 * @author Jacob Jonas
 * @version 2.0
 * @date 3-25-23
 */
public class EyeBreakGUI {
    public static int MILLIS = 20*1000;
    //public static int MILLIS = 20;
    private JButton startButton, stopButton;
    private JLabel instructionLabel;
    private JProgressBar progressBar;
    private boolean running = false;
    /**
     * Instantiates a new Eye break gui.
     */
    public EyeBreakGUI() {
        createGUI();
    }

    /**
     * It creates a GUI with a start and stop button, a progress bar, and a label that displays instructions
     */
    private void createGUI() {
        JFrame frame = new JFrame("Eye Break Program");
        frame.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);





        instructionLabel = new JLabel("Click start to begin the eye break program");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(instructionLabel, BorderLayout.NORTH);
        addMinuteField(frame);
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
            // An action listener that is called when the start button is pressed.
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

    private void addMinuteField(JFrame frame) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel minuteLabel = new JLabel("Minutes:");
        panel.add(minuteLabel);

        JTextField minuteField = new JTextField(2);
        minuteField.setText("20"); // default value
        panel.add(minuteField);

        JButton setButton = new JButton("Set");
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = minuteField.getText();
                try {
                    int minutes = Integer.parseInt(text);
                    MILLIS = minutes * 1000; // update MILLIS field with new value in milliseconds
                    JOptionPane.showMessageDialog(null, "Eye break duration has been set to " + minutes + " minutes.", "Eye Break Duration Set", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(setButton);

        frame.add(panel, BorderLayout.EAST);

    }

    /**
     * It creates a new thread that runs a while loop that increments a progress bar until it reaches 100, then it displays
     * a dialog box that tells the user to take an eye break, then it resets the progress bar to 0 and starts the loop over
     * again
     */
    private void startEyeBreakProgram() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (running) {
                        for (int i = 0; i <= 100 && running; i++) {
                            progressBar.setValue(i);
                            Thread.sleep(MILLIS); // 20 minutes
                        }
                        if (running) {
                            Toolkit.getDefaultToolkit().beep();
                            String message = "                                                        Take a eye break from your screen!";
                            String title = "!!!Eye Break!!!";
                            int messageType = JOptionPane.PLAIN_MESSAGE;


                            JOptionPane pane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);

                            // Customize the JOptionPane size
                            Dimension size = new Dimension(550, 300);
                            pane.setPreferredSize(size);
                            pane.setMaximumSize(size);
                            pane.setMinimumSize(size);

                            JDialog dialog = pane.createDialog(null, title);
                            dialog.setModal(true);
                            dialog.setAlwaysOnTop(true);

                            // Add the gif and rainbow background
                            setEyeBreakDialog(dialog);

                            // add a button that closes the window and says 'okay'
                            JButton okayButton = new JButton("Okay");
                            okayButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    dialog.dispose(); // close the window
                                }
                            });
                            pane.setOptions(new Object[]{okayButton}); // add the button to the dialog

                            dialog.setVisible(true);
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
                    progressBar.setValue(0);
                }
            }
        }).start();
    }



    private void setEyeBreakDialog(JDialog dialog) {
        // Load the GIF image and resize it to fit into the screen
        ImageIcon gifIcon = new ImageIcon(getClass().getResource("eye.gif"));
        Image gifImage = gifIcon.getImage().getScaledInstance(
                (int)(dialog.getSize().getWidth() * 0.4), -2, Image.SCALE_DEFAULT);
        gifIcon = new ImageIcon(gifImage);

        // Create a panel to display the GIF
        JPanel gifPanel = new JPanel(new BorderLayout());
        JLabel gifLabel = new JLabel(gifIcon);
        gifPanel.add(gifLabel, BorderLayout.CENTER);
        dialog.getContentPane().add(gifPanel, BorderLayout.NORTH);


    }




    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EyeBreakGUI();
            }
        });
    }
}