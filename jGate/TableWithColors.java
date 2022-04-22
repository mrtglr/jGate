package jGate;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TableWithColors {
    protected static JTable createTable(DefaultTableModel tableModel) {

        JTable t = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                if(convertColumnIndexToModel(column)==2) return String.class;
                return super.getColumnClass(column);
            }
        };
        t.setDefaultRenderer(String.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                c.setForeground(((String) value)=="locked" ? Color.GREEN : Color.RED);
                return c;
            }
        });
        return t;
    }

    /*
    private static JFrame createFrame() {
        JFrame f = new JFrame("Table with colors");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());
        f.add(new JScrollPane(createTable()),BorderLayout.CENTER);
        f.setSize(new Dimension(60,255));
        return f;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createFrame().setVisible(true);
            }
        });
    }
    */
}