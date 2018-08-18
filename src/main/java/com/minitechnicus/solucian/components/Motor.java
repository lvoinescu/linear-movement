package com.minitechnicus.solucian.components;

import com.minitechnicus.solucian.machine.StepDirection;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class Motor extends Rotatable {

    private final double stepAngle;
    private final int numberOfSteps;


    private double anglePosition;
    private Set<Rotatable> observers = new LinkedHashSet<>();

    Motor(double stepAngle, int numberOfSteps) {
        this.stepAngle = stepAngle;
        this.numberOfSteps = numberOfSteps;
    }

    public void moveStep(StepDirection stepDirection) {
        double rotationAngle = stepDirection == StepDirection.CLOCKWISE ? stepAngle : -stepAngle;
        this.rotate(rotationAngle);
        this.anglePosition += rotationAngle;
    }

    void rotate(double angle) {
        anglePosition += angle;
        observers.forEach(e -> e.rotate(angle));
    }

    public void attach(Rotatable rotatable) {
        observers.add(rotatable);
    }
}
