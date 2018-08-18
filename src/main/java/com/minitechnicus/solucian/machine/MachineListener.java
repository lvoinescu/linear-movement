package com.minitechnicus.solucian.machine;

import com.minitechnicus.solucian.components.MachineState;

public interface MachineListener {

    void stateChanged(MachineState machineState);
}
