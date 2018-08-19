package com.minitechnicus.solucian.machine;

import com.minitechnicus.solucian.components.Machine;

public interface Driver {

    void driveToPoint(double destination);

    void setMaxAngularSpeed(double maxAngularSpeed);

    void driveToZero();

    double getCurrentSpeed();

    void attachToMachine(Machine machine);
}
