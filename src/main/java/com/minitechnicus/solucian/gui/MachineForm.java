package com.minitechnicus.solucian.gui;

import com.minitechnicus.solucian.components.Machine;
import com.minitechnicus.solucian.components.MachineBuildDirector;
import com.minitechnicus.solucian.machine.Driver;
import com.minitechnicus.solucian.machine.FixedSpeedDriver;
import com.minitechnicus.solucian.machine.SmoothRideDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MachineForm implements ActionListener {
    public static final int MAX_ANGULAR_SPEED = 360;
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JPanel simulationPanel;
    private JTextField angularSpeedInput;
    private JRadioButton fixedSpeedRadio;
    private JRadioButton smoothRideRadio;
    private JLabel positionLabel;
    private JButton moveButton;
    private JTextField positionInput;
    private JButton resetButton;
    private JTextField maxAngularAccelerationInput;

    private Driver fixedDriver;
    private SmoothRideDriver smoothDriver;
    private Machine machine;


    private MachineForm(Machine machine) {
        this.machine = machine;
        this.fixedDriver = new FixedSpeedDriver(MAX_ANGULAR_SPEED);
        this.smoothDriver = new SmoothRideDriver(40, 180, 2);
        moveButton.addActionListener(this);
        resetButton.addActionListener(this);
        fixedSpeedRadio.addActionListener(this);
        smoothRideRadio.addActionListener(this);
        maxAngularAccelerationInput.addActionListener(e -> {
            smoothDriver.setAngularAcceleration(Double.parseDouble(maxAngularAccelerationInput.getText()));

        });

        angularSpeedInput.addActionListener(e -> {
            fixedDriver.setMaxAngularSpeed(Double.parseDouble(angularSpeedInput.getText()));
            smoothDriver.setMaxAngularSpeed(Double.parseDouble(angularSpeedInput.getText()));
        });

        smoothDriver.setMaxAngularSpeed(Double.parseDouble(angularSpeedInput.getText()));
        fixedDriver.setMaxAngularSpeed(Double.parseDouble(angularSpeedInput.getText()));
        smoothDriver.setAngularAcceleration(Double.parseDouble(maxAngularAccelerationInput.getText()));
        machine.attachDriver(fixedDriver);
    }

    public static void main(String[] args) {
        MachineBuildDirector machineBuildDirector = new MachineBuildDirector();
        Machine machine = machineBuildDirector.buildMachine(1.8D, 200, 100);
        machine.addObserver((e) -> System.out.println(e.getConveyorPosition()));

        JFrame jFrame = new JFrame("Conveyor belt");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jFrame.getContentPane().add(new MachineForm(machine).mainPanel);
        jFrame.pack();
        jFrame.setSize(1400, 400);
        jFrame.setVisible(true);
    }

    public void createUIComponents() {
        simulationPanel = new SimulationPanel(machine);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "move":
                new Thread(() -> machine.moveToPoint(Double.parseDouble(positionInput.getText()))).start();
                break;
            case "reset":
                new Thread(() -> machine.resetToZeroPosition()).start();
                break;
            case "fixed-driver":
                machine.attachDriver(fixedDriver);
                maxAngularAccelerationInput.setEnabled(false);
                break;
            case "smooth-driver":
                machine.attachDriver(smoothDriver);
                maxAngularAccelerationInput.setEnabled(true);
                break;
        }
    }
}
