package com.minitechnicus.solucian.machine;

import com.minitechnicus.solucian.components.Machine;

public class Driver {

    private final Machine machine;
    private double maxSpeed;
    private final SpeedCalculator speedCalculator;


    public Driver(Machine machine, double maxSpeed) {
        this.machine = machine;
        this.maxSpeed = maxSpeed;
        speedCalculator = new SpeedCalculator();
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public synchronized void moveToPoint(double destination) {

        Double stepAngle = machine.getMotor().getStepAngle();
        Double distancePerStepAngle = machine.getWheel().computeDeltaDistance(stepAngle);

        int noOfRequiredSteps = (int) ((Math.abs(destination - machine.getConveyorBelt().getCurrentPosition())) / distancePerStepAngle);

        StepDirection stepDirection = (destination > machine.getConveyorBelt().getCurrentPosition()) ? StepDirection.CLOCKWISE : StepDirection.COUNTER_CLOCKWISE;
        for (int i = 0; i < noOfRequiredSteps; i++) {
            try {
                Thread.sleep(speedCalculator.rotationSpeedToDelay(machine.getMotor().getStepAngle(), maxSpeed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            machine.getMotor().moveStep(stepDirection);
        }
    }

    public synchronized void resetToZeroPosition() {
        double destination = 0.0d;
        Double stepAngle = machine.getMotor().getStepAngle();
        Double distancePerStepAngle = machine.getWheel().computeDeltaDistance(stepAngle);

        int noOfRequiredSteps = (int) ((Math.abs(destination - machine.getConveyorBelt().getCurrentPosition())) / distancePerStepAngle);

        StepDirection stepDirection = (destination > machine.getConveyorBelt().getCurrentPosition()) ? StepDirection.CLOCKWISE : StepDirection.COUNTER_CLOCKWISE;
        for (int i = 0; i < noOfRequiredSteps; i++) {
            try {
                Thread.sleep(speedCalculator.rotationSpeedToDelay(machine.getMotor().getStepAngle(), maxSpeed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            machine.getMotor().moveStep(stepDirection);
        }
    }
}
