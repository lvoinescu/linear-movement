package com.minitechnicus.solucian.machine;

public interface Driver {

    void moveToPoint(double destination);

    void setMaxAngularSpeed(double maxAngularSpeed);

    void resetToZeroPosition();
}
