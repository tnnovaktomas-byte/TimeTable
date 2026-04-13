package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.ITimetableProvider;
import cz.uhk.timetable.utils.MockTimetableProvider;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class TimetableFrame extends JFrame {
    private ITimetableProvider timetableProvider = new MockTimetableProvider();
    private LocationTimetable timetable;
    private JTable tabTimetable;
    private TimetableModel timetableModel;

    public TimetableFrame() {
        super("Location Timetable");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timetable = timetableProvider.readTimetable("J", "J22");

        initGui();
    }

    private void initGui() {
        timetableModel = new TimetableModel();
        tabTimetable = new JTable(timetableModel);
        add(new JScrollPane(tabTimetable), BorderLayout.CENTER);

        pack();
    }

    class TimetableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return timetable.getActivities().size();
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var a = timetable.getActivities().get(rowIndex);
            switch (columnIndex) {
                case 0 : return a.getId();
                case 1 : return a.getName();
                case 2 : return a.getTeacher();
                case 3 : return a.getType();
                case 4 : return a.getDay();
                case 5 : return a.getStart();
                case 6 : return a.getEnd();
            }
            return "";
        }
    }
}
