package com.minitechnicus.solucian.components;

import lombok.Getter;

@Getter
public class ConveyorBelt implements LinearShifter {

    private double currentPosition;

    @Override
    public void move(double deltaDistance) {
        currentPosition += deltaDistance;
    }
}
