package com.minitechnicus.solucian.gui;

import com.minitechnicus.solucian.components.Machine;
import com.minitechnicus.solucian.components.MachineState;
import com.minitechnicus.solucian.machine.Driver;
import com.minitechnicus.solucian.components.MachineListener;
import com.minitechnicus.solucian.machine.FixedSpeedDriver;
import com.minitechnicus.solucian.machine.SmoothRideDriver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MachineWindow extends JPanel implements MachineListener, ActionListener {

    private Machine machine;
    private Driver driver;
    private JTextField angleSpeedInput;
    private JTextField moveToPointInput;
    private BufferedImage image;

    MachineWindow(Machine machine) {
        super();
        this.machine = machine;
        machine.addObserver(this);
        driver = new FixedSpeedDriver(machine, 180);

        JButton moveButton = new JButton("Move");
        moveButton.setVerticalTextPosition(AbstractButton.CENTER);
        moveButton.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        moveButton.setMnemonic(KeyEvent.VK_D);
        moveButton.setActionCommand("move");
        moveButton.addActionListener(this);

        JButton resetButton = new JButton("Reset");
        resetButton.setVerticalTextPosition(AbstractButton.CENTER);
        resetButton.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        resetButton.setMnemonic(KeyEvent.VK_D);
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);


        angleSpeedInput = new JTextField(8);
        angleSpeedInput.setText("180");
        angleSpeedInput.setFont(angleSpeedInput.getFont().deriveFont(10f));
        this.add(angleSpeedInput);

        moveToPointInput = new JTextField(8);
        moveToPointInput.setText("600");
        moveToPointInput.setFont(moveToPointInput.getFont().deriveFont(10f));
        this.add(moveToPointInput);

        this.add(moveButton);
        this.add(resetButton);

        ButtonGroup group = new ButtonGroup();
        JRadioButton btn1 = new JRadioButton("Fixed-speed driver ");
        btn1.setSelected(true);
        btn1.setActionCommand("default-driver");
        btn1.addActionListener(this);
        JRadioButton btn2 = new JRadioButton("Smooth ride driver ");
        btn2.setActionCommand("smooth-driver");
        btn2.addActionListener(this);


        group.add(btn1);
        group.add(btn2);

        this.add(btn1);
        this.add(btn2);


        try {
            image = ImageIO.read(new File(getClass().getClassLoader().getResource("gear.png").toURI()));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.CYAN);


        g.fillRect(60, 210, (int) machine.getState().getConveyorPosition() - 60, 3);
        g.fill3DRect((int) machine.getState().getConveyorPosition(), 200, 150, 20, true);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.RED);
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

        g2d.setColor(Color.BLACK);
        AffineTransform at = new AffineTransform();
        at.setToRotation(Math.toRadians(machine.getMotor().getAnglePosition()), (image.getWidth() / 2), 200 + (image.getHeight() / 2));
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 200, this);
        g2d.dispose();

        g.drawString("Current position:" + Math.round(machine.getConveyorBelt().getCurrentPosition()), 10, 340);
        g.drawString("Current speed:" + Math.round(driver.getCurrentSpeed()), 10, 355);
        g.drawString("Current motor angle:" + Math.round(machine.getMotor().getAnglePosition()), 10, 370);
    }

    @Override
    public void stateChanged(MachineState machineState) {
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("move".equals(e.getActionCommand())) {
            driver.setMaxAngularSpeed(Double.parseDouble(angleSpeedInput.getText()));
            new Thread(() -> driver.moveToPoint(Double.parseDouble(moveToPointInput.getText()))).start();
        }
        if ("reset".equals(e.getActionCommand())) {
            new Thread(() -> driver.resetToZeroPosition()).start();
        }

        if ("default-driver".equals(e.getActionCommand())) {
            driver = new FixedSpeedDriver(machine, 180);
        }

        if ("smooth-driver".equals(e.getActionCommand())) {
            driver = new SmoothRideDriver(machine, 40, 180, 2);
        }
    }
}
