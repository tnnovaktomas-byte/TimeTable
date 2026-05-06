package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.ITimetableProvider;
import cz.uhk.timetable.utils.MockTimetableProvider;
import cz.uhk.timetable.utils.StagTimetableProvider;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class TimetableFrame extends JFrame {
    private ITimetableProvider timetableProvider = new StagTimetableProvider();
    private LocationTimetable timetable;
    private JTable tabTimetable;
    private TimetableModel timetableModel;
    private JComboBox buildingBox;
    private JComboBox roomBox;

    //The URL for the new STAG shite: https://stag-demo.uhk.cz/ws/services/rest2/mistnost/getMistnostiInfo?zkrBudovy=%&pracoviste=%&typ=U&outputFormat=JSON&cisloMistnosti=% (Taken 25 after % bcs ascii)

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

        // Toolbar for picking building
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Initialize and populate the combo box
        buildingBox = new JComboBox<>(new String[]{"J", "B", "C"});
        buildingBox.addActionListener(e -> {
            String selectedBuilding = (String) buildingBox.getSelectedItem();
            timetable = timetableProvider.readTimetable(selectedBuilding, ""); // adjust room as needed
            timetableModel.fireTableDataChanged(); // refresh the table
        });

        roomBox = new JComboBox<>(new String[]{"J22", "J10"});
        roomBox.addActionListener(e -> {
            String selectedRoom = (String) roomBox.getSelectedItem();
            timetable = timetableProvider.readTimetable("", selectedRoom);
            timetableModel.fireTableDataChanged();
        });

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> {
            String selectedBuilding = (String) buildingBox.getSelectedItem();
            String selectedRoom = (String) roomBox.getSelectedItem();
            timetable = timetableProvider.readTimetable(selectedBuilding, selectedRoom);
            timetableModel.fireTableDataChanged();
        });

        toolBar.add(new JLabel("Building: "));
        toolBar.add(buildingBox);
        toolBar.addSeparator();
        toolBar.add(roomBox);
        toolBar.addSeparator();
        toolBar.add(btnRefresh);

        add(toolBar, BorderLayout.NORTH);

        pack();
        setVisible(true);
    }

    //cez toolbar

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
