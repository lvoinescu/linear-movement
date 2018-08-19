package com.minitechnicus.solucian.machine;

import com.minitechnicus.solucian.components.ConveyorBelt;
import com.minitechnicus.solucian.components.Motor;
import com.minitechnicus.solucian.components.Rotatable;
import com.minitechnicus.solucian.components.Wheel;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class Machine {

    private final Motor motor;
    private final Wheel wheel;
    private final ConveyorBelt conveyorBelt;
    private Driver driver;
    private final Set<MachineListener> machineListeners = new LinkedHashSet<>();

    Machine(Motor motor,
            Wheel wheel,
            ConveyorBelt conveyorBelt) {
        this.motor = motor;
        this.wheel = wheel;
        this.conveyorBelt = conveyorBelt;
        this.motor.attach(new Rotatable() {

            @Override
            public void rotate(double angle) {
                Machine.this.machineListeners.forEach(e -> e.stateChanged(
                        new MachineState(conveyorBelt.getCurrentPosition(), motor.getAnglePosition(), driver.getCurrentSpeed())));
            }
        });
    }

    public void addObserver(MachineListener machineListener) {
        this.machineListeners.add(machineListener);
    }

    public void attachDriver(Driver driver) {
        this.driver = driver;
        driver.attachToMachine(this);
    }

    public void moveToPoint(double destination) {
        driver.driveToPoint(destination);
        machineListeners.forEach(e -> e.stateChanged(getState()));
    }

    public MachineState getState() {
        return new MachineState(conveyorBelt.getCurrentPosition(), motor.getAnglePosition(), driver.getCurrentSpeed());
    }

    public void resetToZeroPosition() {
        driver.driveToZero();
    }
}
