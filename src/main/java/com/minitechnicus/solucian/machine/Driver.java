package com.minitechnicus.solucian.machine;

public interface Driver {

    void driveToPoint(double destination);

    void setMaxAngularSpeed(double maxAngularSpeed);

    void driveToZero();

    double getCurrentSpeed();

    void attachToMachine(Machine machine);
}
