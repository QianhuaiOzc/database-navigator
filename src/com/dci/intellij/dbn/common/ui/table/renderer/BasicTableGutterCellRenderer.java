package com.dci.intellij.dbn.common.ui.table.renderer;

import com.dci.intellij.dbn.common.ui.table.model.BasicDataModel;
import com.dci.intellij.dbn.common.ui.table.model.DataModelRow;
import com.intellij.util.ui.UIUtil;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class BasicTableGutterCellRenderer extends JPanel implements ListCellRenderer {
    private static final Border BORDER = new CompoundBorder(
            UIManager.getBorder("TableHeader.cellBorder"),
            new EmptyBorder(0, 2, 0, 6));

    private JLabel lText;

    public static final ListCellRenderer INSTANCE = new BasicTableGutterCellRenderer();

    public BasicTableGutterCellRenderer() {
        setForeground(Color.BLACK);
        setBackground(UIUtil.getPanelBackground());
        setBorder(BORDER);
        setLayout(new BorderLayout());
        lText = new JLabel();
        add(lText, BorderLayout.WEST);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        BasicDataModel model = (BasicDataModel) list.getModel();
        DataModelRow row = model.getRowAtIndex(index);
        lText.setText("" + row.getIndex());
        //lText.setFont(isSelected ? BOLD_FONT : REGULAR_FONT);
        lText.setForeground(isSelected ? Color.WHITE : Color.BLACK);
        return this;
    }
}