package com.minitechnicus.solucian.components;

public class MachineBuildDirector {

    public Machine buildMachine(double stepperMotorAngle, int stepperMotorMaxSteps, double wheelRadius) {
        Motor motor = new Motor(stepperMotorAngle, stepperMotorMaxSteps);

        ConveyorBelt conveyorBelt = new ConveyorBelt();
        Wheel wheel = new Wheel(wheelRadius);
        wheel.attach(conveyorBelt);
        motor.attach(wheel);

        Machine machine = new Machine(motor, wheel, conveyorBelt);
        return machine;
    }
}
