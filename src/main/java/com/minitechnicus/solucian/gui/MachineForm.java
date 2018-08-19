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

    private Driver driver;
    private Machine machine;


    private MachineForm(Machine machine) {
        this.machine = machine;
        this.driver = new FixedSpeedDriver(machine, 360);
        moveButton.addActionListener(this);
        resetButton.addActionListener(this);
        fixedSpeedRadio.addActionListener(this);
        smoothRideRadio.addActionListener(this);
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
        if ("move".equals(e.getActionCommand())) {
            driver.setMaxAngularSpeed(Double.parseDouble(angularSpeedInput.getText()));
            new Thread(() -> driver.moveToPoint(Double.parseDouble(positionInput.getText()))).start();
        }
        if ("reset".equals(e.getActionCommand())) {
            new Thread(() -> driver.resetToZeroPosition()).start();
        }

        if ("fixed-driver".equals(e.getActionCommand())) {
            driver = new FixedSpeedDriver(machine, 180);
        }

        if ("smooth-driver".equals(e.getActionCommand())) {
            driver = new SmoothRideDriver(machine, 40, 180, 2);
        }
    }
}
