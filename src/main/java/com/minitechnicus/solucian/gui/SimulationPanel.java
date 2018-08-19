package com.minitechnicus.solucian.gui;

import com.minitechnicus.solucian.components.Machine;
import com.minitechnicus.solucian.components.MachineListener;
import com.minitechnicus.solucian.components.MachineState;

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
    private BufferedImage image;
    private MachineState machineState;

    SimulationPanel(Machine machine) {
        super(new GridBagLayout());
        this.machine = machine;
        this.setDoubleBuffered(true);
        this.machine.addObserver(this);

        try {
            image = ImageIO.read(new File(getClass().getClassLoader().getResource("gear.png").toURI()));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
        this.machineState = new MachineState(0, 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(70, 210, (int) machine.getState().getConveyorPosition() - 60, 3);
        g.fill3DRect((int) machine.getState().getConveyorPosition(), 200, 150, 20, true);

        Graphics2D g2d = (Graphics2D) g.create();

        AffineTransform at = new AffineTransform();
        at.setToRotation(Math.toRadians(machine.getMotor().getAnglePosition()), (image.getWidth() / 2), 200 + (image.getHeight() / 2));
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 200, this);
        g2d.dispose();

        g.drawString("Current position:" + Math.round(machineState.getConveyorPosition()), 10, 340);
        g.drawString("Current speed:" + Math.round(machineState.getCurrentSpeed()), 10, 355);
        g.drawString("Current motor angle:" + Math.round(machineState.getMotorAngle()), 10, 370);
    }

    @Override
    public void stateChanged(MachineState machineState) {
        this.machineState = machineState;
        repaint();
    }
}
