package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.Activity;
import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.ITimetableProvider;
import cz.uhk.timetable.utils.StagRoomProvider;
import cz.uhk.timetable.utils.StagTimetableProvider;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class DayFrame extends JFrame {
    private ITimetableProvider timetableProvider = new StagTimetableProvider();
    private LocationTimetable timetable;
    private JTable tabTimetable;
    private TimetableModel timetableModel;
    private JComboBox buildingBox;
    private JComboBox roomBox;
    private StagRoomProvider roomProvider = new StagRoomProvider();

    public DayFrame() {
        super("Location Timetable");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timetable = timetableProvider.readTimetable("A", "AULA");

        initGui();
    }

    private void initGui() {
        timetableModel = new TimetableModel();
        tabTimetable = new JTable(timetableModel);
        add(new JScrollPane(tabTimetable), BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        buildingBox = new JComboBox<>(new String[]{"A", "B", "C", "H", "J"});
        buildingBox.addActionListener(e -> {
            String selectedBuilding = (String) buildingBox.getSelectedItem();
            updateRoomList(selectedBuilding);
        });

        roomBox = new JComboBox<>();
        roomBox.addActionListener(e -> {
            String selectedRoom = (String) roomBox.getSelectedItem();
            String selectedBuilding = (String) buildingBox.getSelectedItem();
            if (selectedRoom != null) {
                timetable = timetableProvider.readTimetable(selectedBuilding, selectedRoom);
            }
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
        toolBar.add(new JLabel("Room: "));
        toolBar.add(roomBox);
        toolBar.addSeparator();
        toolBar.add(btnRefresh);

        add(toolBar, BorderLayout.NORTH);

        updateRoomList((String) buildingBox.getSelectedItem());

        pack();
        setVisible(true);
    }

    /**
     * Bridges StagRoomProvider and roomBox.
     */
    private void updateRoomList(String building) {
        StagRoomProvider roomProvider = new StagRoomProvider();
        java.util.List<cz.uhk.timetable.model.RoomNumber> rooms = roomProvider.getRooms(building);

        rooms.sort((r1, r2) -> r1.getRoomID().compareToIgnoreCase(r2.getRoomID()));

        var listeners = roomBox.getActionListeners();
        for (var l : listeners) roomBox.removeActionListener(l);

        roomBox.removeAllItems();

        for (var room : rooms) {
            roomBox.addItem(room.getRoomID());
        }

        for (var l : listeners) roomBox.addActionListener(l);
    }

    class TimetableModel extends AbstractTableModel {

        private static final String[] COLNAMES = {
                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
        };

        @Override
        public int getRowCount() {
            int max = 0;
            for (int i = 0; i <7; i++) {
                max = Math.max(max, getClassesForDay(i).size());
            }
            return max;
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public String getColumnName(int column) {
            return COLNAMES[column];
        }

        private String getDayNameForColumn(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> "Pondělí";
                case 1 -> "Úterý";
                case 2 -> "Středa";
                case 3 -> "Čtvrtek";
                case 4 -> "Pátek";
                case 5 -> "Sobota";
                case 6 -> "Neděle";
                default -> "";
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var classesForDay = getClassesForDay(columnIndex);
            if (rowIndex < classesForDay.size()) {
                return classesForDay.get(rowIndex).getId();
            }
            return "";
        }

        private List<Activity> getClassesForDay(int columnIndex) {
            String targetDay = getDayNameForColumn(columnIndex);
            return timetable.getActivities().stream().filter(a -> a.getDay().equals(targetDay)).toList();
        }
    }
}
