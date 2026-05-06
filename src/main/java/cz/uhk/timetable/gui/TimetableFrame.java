package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.utils.ITimetableProvider;
import cz.uhk.timetable.utils.MockTimetableProvider;
import cz.uhk.timetable.utils.StagRoomProvider;
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
    private StagRoomProvider roomProvider = new StagRoomProvider();

    public TimetableFrame() {
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
