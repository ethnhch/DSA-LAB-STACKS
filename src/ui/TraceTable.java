package ui;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/** Read-only dark table used to show step-by-step traces with active step highlighting. */
public class TraceTable extends JScrollPane {
    private final DefaultTableModel model;
    private final JTable table;
    private int activeRow = -1;

    public TraceTable(String... columns) {
        this(new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private TraceTable(DefaultTableModel model) {
        super(new JTable(model));
        this.model = model;
        this.table = (JTable) getViewport().getView();

        table.setRowHeight(34);
        table.setFont(Theme.MONO_SM.deriveFont(12.5f));
        table.setBackground(Theme.PANEL);
        table.setForeground(Theme.TEXT);
        table.setGridColor(Theme.LINE_MUTED);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                // Center step/index, left-align descriptive content
                if (c == 0 || (tbl.getColumnCount() > 1 && c == 1 && val != null && val.toString().length() <= 3)) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

                if (r == activeRow) {
                    comp.setBackground(new Color(0x272B56));
                    comp.setForeground(Color.WHITE);
                } else {
                    comp.setBackground(r % 2 == 0 ? Theme.PANEL : Theme.PANEL_ALT);
                    comp.setForeground(Theme.TEXT_SUB);
                }
                return comp;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0x161C2C));
        header.setForeground(Theme.ACCENT_BRIGHT);
        header.setFont(Theme.BOLD.deriveFont(12f));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                comp.setBackground(new Color(0x161C2C));
                comp.setForeground(Theme.ACCENT_BRIGHT);
                setFont(Theme.BOLD.deriveFont(12f));
                if (c == 0 || (tbl.getColumnCount() > 1 && c == 1 && tbl.getColumnName(c).equalsIgnoreCase("Symbol"))) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
                return comp;
            }
        });

        setBorder(BorderFactory.createLineBorder(Theme.LINE, 1));
        getViewport().setBackground(Theme.PANEL);
    }

    public void clear() {
        activeRow = -1;
        model.setRowCount(0);
    }

    public void addRow(String[] row) {
        model.addRow(row);
        highlightRow(model.getRowCount() - 1);
    }

    public void highlightRow(int row) {
        this.activeRow = row;
        if (row >= 0 && row < model.getRowCount()) {
            table.setRowSelectionInterval(row, row);
            table.scrollRectToVisible(table.getCellRect(row, 0, true));
        } else {
            table.clearSelection();
        }
        table.repaint();
    }

    public void setRows(List<String[]> rows) {
        clear();
        rows.forEach(model::addRow);
        if (model.getRowCount() > 0) highlightRow(model.getRowCount() - 1);
    }

    public int getRowCount() {
        return model.getRowCount();
    }
}


