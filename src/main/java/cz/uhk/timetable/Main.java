package cz.uhk.timetable;

import cz.uhk.timetable.gui.DayFrame;
import cz.uhk.timetable.gui.TimetableFrame;

import javax.swing.*;

public class Main {
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TimetableFrame().setVisible(true));
        SwingUtilities.invokeLater(() -> new DayFrame().setVisible(true));
    }
}
