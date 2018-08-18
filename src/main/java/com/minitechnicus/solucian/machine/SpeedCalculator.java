package com.minitechnicus.solucian.machine;

public class SpeedCalculator {

    /**
     * @param speed - speed of the motor in degrees/sec; eg. 180 means 180 [deg./sec]
     * @return sleep amount between steps in ms
     */
    public int rotationSpeedToDelay(double stepAngle, double speed) {
        return (int) ((1000 / (speed / stepAngle)));
    }
}
