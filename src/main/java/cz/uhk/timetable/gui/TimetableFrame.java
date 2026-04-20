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

        private static final String[] COLNAMES = {
                "ZKRATKA", "NAZEV", "UCITEL", "TYP", "DEN", "ZACATEK", "KONEC"
        };

        @Override
        public int getRowCount() {
            return timetable.getActivities().size();
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public String getColumnName(int column) {
            return COLNAMES[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var a = timetable.getActivities().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> a.getId();
                case 1 -> a.getName();
                case 2 -> a.getTeacher();
                case 3 -> a.getType();
                case 4 -> a.getDay();
                case 5 -> a.getStart();
                case 6 -> a.getEnd();
                default -> "";
            };
        }
    }
}
