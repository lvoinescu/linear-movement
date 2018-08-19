package com.minitechnicus.solucian.machine;

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

    private double speed;

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

    @Override
    public synchronized void driveToPoint(double destination) {
        int noOfStepsForDistance = (int) (Math.abs(destination - machine.getConveyorBelt().getCurrentPosition()) /
                machine.getWheel().computeDeltaDistance(machine.getMotor().getStepAngle()));

        int noOfStepsNeededForMaxSpeed = (int) (maxAngularSpeed / angularAcceleration);

        StepDirection stepDirection = (destination > machine.getConveyorBelt().getCurrentPosition()) ? StepDirection.CLOCKWISE : StepDirection.COUNTER_CLOCKWISE;
        for (int i = 1; i <= noOfStepsForDistance; i++) {
            try {
                currentSpeed = getSpeed(i, noOfStepsForDistance, noOfStepsNeededForMaxSpeed);
                Thread.sleep(speedCalculator.rotationSpeedToDelay(machine.getMotor().getStepAngle(), currentSpeed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            machine.getMotor().moveStep(stepDirection);
        }
        currentSpeed = 0;
        speed = 0;
    }

    /**
     * In case there is no time to reach full speed, a triangle shape will be observed
     */
    private double getSpeed(int stepNumber, int stepsToDo, int noOfStepsNeededForMaxSpeed) {
        int changeStep = stepsToDo / 2;

        if (stepNumber < changeStep && stepNumber <= noOfStepsNeededForMaxSpeed) {
            speed = stepNumber * angularAcceleration;
        } else {
            if (stepNumber > stepsToDo - changeStep && stepNumber > stepsToDo - noOfStepsNeededForMaxSpeed) {
                speed = (stepsToDo - stepNumber) * angularAcceleration;
            }
        }
        return ensureMinimalSpeed(speed);
    }

    private double ensureMinimalSpeed(double speed) {
        return speed < minAngularSpeed ? minAngularSpeed : speed;
    }
}
