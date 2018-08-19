package com.minitechnicus.solucian.machine;

import com.minitechnicus.solucian.components.Machine;

public class SmoothRideDriver implements Driver {

    private Machine machine;
    private final SpeedCalculator speedCalculator;
    private double maxAngularSpeed;
    private final double minAngularSpeed;
    private double angularAcceleration;
    private volatile double currentSpeed;

    public SmoothRideDriver(double minAngularSpeed,
                            double maxAngularSpeed,
                            double angularAcceleration) {
        this.maxAngularSpeed = maxAngularSpeed;
        this.minAngularSpeed = minAngularSpeed;
        this.angularAcceleration = angularAcceleration;
        speedCalculator = new SpeedCalculator();
    }

    @Override
    public void driveToPoint(double destination) {
        int noOfStepsForDistance = (int) (Math.abs(destination - machine.getConveyorBelt().getCurrentPosition()) /
                machine.getWheel().computeDeltaDistance(machine.getMotor().getStepAngle()));

        StepDirection stepDirection = (destination > machine.getConveyorBelt().getCurrentPosition()) ? StepDirection.CLOCKWISE : StepDirection.COUNTER_CLOCKWISE;
        for (int i = 0; i <= noOfStepsForDistance; i++) {
            try {
                currentSpeed = getSpeed(i, noOfStepsForDistance);
                Thread.sleep(speedCalculator.rotationSpeedToDelay(machine.getMotor().getStepAngle(), currentSpeed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            machine.getMotor().moveStep(stepDirection);
        }
        currentSpeed = 0;
    }

    @Override
    public void setMaxAngularSpeed(double maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    @Override
    public void driveToZero() {
        driveToPoint(0.0d);
    }

    @Override
    public double getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public void attachToMachine(Machine machine) {
        this.machine = machine;
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
