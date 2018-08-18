package com.minitechnicus.solucian.gui;

import com.minitechnicus.solucian.components.Machine;
import com.minitechnicus.solucian.components.MachineBuildDirector;

import javax.swing.*;

public class MachineExperiment {

    public static void main(String[] args) {
        MachineBuildDirector machineBuildDirector = new MachineBuildDirector();
        Machine machine = machineBuildDirector.buildMachine(1.8D, 200, 100);
        machine.addObserver((e) -> System.out.println(e.getConveyorPosition()));

        JFrame jFrame = new JFrame("Conveyor belt");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        MachineWindow machineWindow = new MachineWindow(machine);
        jFrame.add(machineWindow);
        jFrame.setSize(1400, 400);
        jFrame.setVisible(true);
    }
}
