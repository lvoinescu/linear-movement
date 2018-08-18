package com.minitechnicus.solucian.components;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MachineState {

    private final double conveyorPosition;
    private final double motorAngle;
}
