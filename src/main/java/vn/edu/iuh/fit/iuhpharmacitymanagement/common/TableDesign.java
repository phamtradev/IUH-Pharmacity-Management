/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author PhamTra
 */
public class TableDesign {

    private DefaultTableModel modelTable;
    private JTable table;
    private TableColumnModel columnModel;

    public TableDesign(String[] headers, List<Integer> tableWidth) {
        initializeTable(headers, tableWidth, null);
    }

    public TableDesign(String[] headers, List<Integer> tableWidth, List<Boolean> canEdit) {
        initializeTable(headers, tableWidth, canEdit);
    }

    private void initializeTable(String[] headers, List<Integer> tableWidth, List<Boolean> canEdit) {
        modelTable = new DefaultTableModel(headers, 0) {
            boolean[] editableStates;

            {
                if (canEdit != null) {
                    editableStates = new boolean[headers.length];
                    if (canEdit.size() == editableStates.length) {
                        for (int i = 0; i < editableStates.length; i++) {
                            editableStates[i] = canEdit.get(i);
                        }
                    } else {
                        throw new IllegalArgumentException("The size of canEdit must match the number of columns.");
                    }
                } else {
                    editableStates = new boolean[headers.length];
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return editableStates[columnIndex];
            }
        };

        table = new JTable(modelTable) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                // Đổi màu cho dòng được chọn               
                if (isRowSelected(row)) {
                    c.setBackground(new Color(0xF5F5F5));
                } else {
                    c.setBackground(getBackground());
                }
                setFont(c.getFont().deriveFont(Font.PLAIN, 15));
//                JLabel label = (JLabel) c;
//                label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 10));
                return c;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);

        // Thiết lập ListSelectionListener
        table.getSelectionModel().addListSelectionListener(e -> {
            table.repaint(); // Vẽ lại bảng khi có sự thay đổi lựa chọn
        });

        setupTableHeader(headers, tableWidth);
        setupTableAppearance();
//        setupMouseListener();
    }

    private void setupTableHeader(String[] headers, List<Integer> tableWidth) {
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 50));
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0xF6F6F6));

        columnModel = table.getColumnModel();
        for (int i = 0; i < headers.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(tableWidth.get(i));
            column.setResizable(false);
        }
    }

    private void setupTableAppearance() {
        table.setRowHeight(50);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(0xE9E9E9));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public DefaultTableModel getModelTable() {
        return modelTable;
    }

    public void setModelTable(DefaultTableModel modelTable) {
        this.modelTable = modelTable;
    }

    public TableColumnModel getColumnModel() {
        return columnModel;
    }

    public void setColumnModel(TableColumnModel columnModel) {
        this.columnModel = columnModel;
    }

    public void setTableHeaderFontSize(Integer size) {
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, size));
    }

}
