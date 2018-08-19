package com.minitechnicus.solucian.gui;

import com.minitechnicus.solucian.components.Machine;
import com.minitechnicus.solucian.components.MachineListener;
import com.minitechnicus.solucian.components.MachineState;
import com.minitechnicus.solucian.machine.Driver;
import com.minitechnicus.solucian.machine.FixedSpeedDriver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SimulationPanel extends JPanel implements MachineListener {

    private Machine machine;
    private Driver driver;
    private BufferedImage image;

    SimulationPanel(Machine machine) {
        super(new GridBagLayout());
        this.machine = machine;
        this.setDoubleBuffered(true);
        this.machine.addObserver(this);
        driver = new FixedSpeedDriver(machine, 180);

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
}
