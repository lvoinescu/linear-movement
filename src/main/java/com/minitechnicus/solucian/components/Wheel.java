package com.minitechnicus.solucian.components;

import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class Wheel extends Rotatable {

    private final double radius;

    private double currentAngle;
    private Set<LinearShifter> linearShifters = new LinkedHashSet<>();

    public Wheel(double radius) {
        this.radius = radius;
    }

    public void rotate(double angle) {
        this.currentAngle += angle;
        double deltaDistance = computeDeltaDistance(angle);
        linearShifters.forEach(s -> s.move(deltaDistance));
    }

    public void attach(LinearShifter linearShifter) {
        linearShifters.add(linearShifter);
    }

    public double computeDeltaDistance(double angle) {
        return Math.PI * radius * (angle / 180);
    }
}
