package com.minitechnicus.solucian.machine;


public class FixedSpeedDriver implements Driver {

    private Machine machine;
    private double maxAngularSpeed;
    private final SpeedCalculator speedCalculator;
    private volatile boolean moving;

    public FixedSpeedDriver(double maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
        this.speedCalculator = new SpeedCalculator();
    }

    public void setMaxAngularSpeed(double maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public void driveToPoint(double destination) {
        doMoveToPoint(destination);
    }

    @Override
    public void driveToZero() {
        double destination = 0.0d;
        doMoveToPoint(destination);
    }

    @Override
    public double getCurrentSpeed() {
        return moving ? maxAngularSpeed : 0.0d;
    }

    @Override
    public void attachToMachine(Machine machine) {
        this.machine = machine;
    }

    private synchronized void doMoveToPoint(double destination) {
        Double stepAngle = machine.getMotor().getStepAngle();
        Double distancePerStepAngle = machine.getWheel().computeDeltaDistance(stepAngle);

        int noOfRequiredSteps = (int) ((Math.abs(destination - machine.getConveyorBelt().getCurrentPosition())) / distancePerStepAngle);

        StepDirection stepDirection = (destination > machine.getConveyorBelt().getCurrentPosition()) ? StepDirection.CLOCKWISE : StepDirection.COUNTER_CLOCKWISE;
        moving = true;
        for (int i = 0; i <= noOfRequiredSteps; i++) {
            try {
                Thread.sleep(speedCalculator.rotationSpeedToDelay(machine.getMotor().getStepAngle(), maxAngularSpeed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            machine.getMotor().moveStep(stepDirection);
        }
        moving = false;
    }
}
