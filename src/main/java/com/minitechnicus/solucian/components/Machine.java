package com.minitechnicus.solucian.components;

import com.minitechnicus.solucian.machine.MachineListener;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class Machine {

    private final Motor motor;
    private final Wheel wheel;
    private final ConveyorBelt conveyorBelt;

    private final Set<MachineListener> machineListeners = new LinkedHashSet<>();

    Machine(Motor motor,
            Wheel wheel,
            ConveyorBelt conveyorBelt) {
        this.motor = motor;
        this.wheel = wheel;
        this.conveyorBelt = conveyorBelt;
        this.motor.attach(new Rotatable() {

            @Override
            void rotate(double angle) {
                Machine.this.machineListeners.forEach(e -> e.stateChanged(
                        new MachineState(conveyorBelt.getCurrentPosition(), motor.getAnglePosition())));
            }
        });
    }

    public void addObserver(MachineListener machineListener){
        this.machineListeners.add(machineListener);
    }

    public MachineState getState(){
        return new MachineState(conveyorBelt.getCurrentPosition(), motor.getAnglePosition());
    }

}
