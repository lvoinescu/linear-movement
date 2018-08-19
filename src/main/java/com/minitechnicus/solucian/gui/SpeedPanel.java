package com.minitechnicus.solucian.gui;

import com.minitechnicus.solucian.components.Machine;
import com.minitechnicus.solucian.components.MachineListener;
import com.minitechnicus.solucian.components.MachineState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class SpeedPanel extends JPanel implements MachineListener {

    private final XYSeries series;
    private int x;

    SpeedPanel(Machine machine) {
        machine.addObserver(this);
        series = new XYSeries("Angular speed variation");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart("Angular speed", "Time (ms)", "grd/sec", dataset);
        ChartPanel chartPanel = new ChartPanel(chart, 1000, 200, 100, 100,
                1000, 200, true, true, true, false, true, true);

        this.add(chartPanel, BorderLayout.SOUTH);
    }

    @Override
    public void stateChanged(MachineState machineState) {
        series.add(x++, machineState.getCurrentSpeed());
    }
}
