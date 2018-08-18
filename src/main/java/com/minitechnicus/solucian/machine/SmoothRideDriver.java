package com.minitechnicus.solucian.machine;

import com.minitechnicus.solucian.components.Machine;

public class SmoothRideDriver implements Driver {

    private final Machine machine;
    private final SpeedCalculator speedCalculator;
    private double maxAngularSpeed;
    private final double minAngularSpeed;
    private final double angularAcceleration;

    public SmoothRideDriver(Machine machine,
                            double minAngularSpeed,
                            double maxAngularSpeed,
                            double angularAcceleration) {
        this.machine = machine;
        this.maxAngularSpeed = maxAngularSpeed;
        this.minAngularSpeed = minAngularSpeed;
        this.angularAcceleration = angularAcceleration;
        speedCalculator = new SpeedCalculator();
    }

    @Override
    public void moveToPoint(double destination) {
        int noOfStepsForDistance = (int) (Math.abs(destination - machine.getConveyorBelt().getCurrentPosition()) /
                machine.getWheel().computeDeltaDistance(machine.getMotor().getStepAngle()));

        StepDirection stepDirection = (destination > machine.getConveyorBelt().getCurrentPosition()) ? StepDirection.CLOCKWISE : StepDirection.COUNTER_CLOCKWISE;
        for (int i = 0; i < noOfStepsForDistance; i++) {
            try {
                double speed = getSpeed(i, noOfStepsForDistance);
                Thread.sleep(speedCalculator.rotationSpeedToDelay(machine.getMotor().getStepAngle(), speed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            machine.getMotor().moveStep(stepDirection);
        }
    }

    @Override
    public void setMaxAngularSpeed(double maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public void resetToZeroPosition() {
        moveToPoint(0.0d);
    }

    private double getSpeed(int stepNumber, int stepsToDo) {
        double speed;
        int changeStep = stepsToDo / 2;
        if (stepNumber < changeStep) {
            speed = minAngularSpeed + stepNumber * angularAcceleration;
        } else {
            if (stepNumber > stepsToDo - changeStep) {
                speed = minAngularSpeed + (stepsToDo - stepNumber) * angularAcceleration;
            } else {
                speed = maxAngularSpeed;
            }
        }
        return speed;
    }
}
