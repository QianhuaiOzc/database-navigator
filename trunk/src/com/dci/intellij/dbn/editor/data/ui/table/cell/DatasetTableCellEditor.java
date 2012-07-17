 package com.dci.intellij.dbn.editor.data.ui.table.cell;

 import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
 import com.dci.intellij.dbn.common.ui.MouseUtil;
 import com.dci.intellij.dbn.common.ui.table.model.ColumnInfo;
 import com.dci.intellij.dbn.data.editor.ui.BasicDataEditorComponent;
 import com.dci.intellij.dbn.data.editor.ui.DataEditorComponent;
 import com.dci.intellij.dbn.data.type.BasicDataType;
 import com.dci.intellij.dbn.data.type.DBDataType;
 import com.dci.intellij.dbn.editor.data.DatasetEditorManager;
 import com.dci.intellij.dbn.editor.data.filter.DatasetFilterInput;
 import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
 import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelCell;
 import com.dci.intellij.dbn.object.DBColumn;
 import com.intellij.ui.SimpleTextAttributes;

 import javax.swing.JTextField;
 import javax.swing.border.Border;
 import javax.swing.border.CompoundBorder;
 import javax.swing.border.EmptyBorder;
 import javax.swing.border.LineBorder;
 import javax.swing.text.Document;
 import java.awt.Color;
 import java.awt.MouseInfo;
 import java.awt.Point;
 import java.awt.event.KeyEvent;
 import java.awt.event.KeyListener;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;

public class DatasetTableCellEditor extends AbstractDatasetTableCellEditor implements KeyListener{
    public static final Border EMPTY_BORDER = new EmptyBorder(0, 3, 0, 3);
    private static final Border ERROR_BORDER = new CompoundBorder(new LineBorder(Color.RED, 1), new EmptyBorder(0, 2, 0, 2));
    private static final Border POPUP_BORDER = new CompoundBorder(new LineBorder(Color.BLUE, 1), new EmptyBorder(0, 2, 0, 2));

    public static final int HIGHLIGHT_TYPE_NONE = 0;
    public static final int HIGHLIGHT_TYPE_POPUP = 1;
    public static final int HIGHLIGHT_TYPE_ERROR = 2;

    private DatasetEditorTable table;
    private DatasetEditorModelCell cell;

    public DatasetTableCellEditor(DatasetEditorTable table) {
        this(table, new BasicDataEditorComponent());
        SimpleTextAttributes selectionTextAttributes = table.getConfigTextAttributes().getSelection();

        JTextField textField = getTextField();
        textField.setSelectionColor(selectionTextAttributes.getBgColor());
        textField.setSelectedTextColor(selectionTextAttributes.getFgColor());
    }

    public DatasetTableCellEditor(DatasetEditorTable table, DataEditorComponent editorComponent) {
        super(editorComponent, table.getProject());
        this.table = table;
        JTextField textField = getTextField();
        textField.addKeyListener(this);
        textField.addMouseListener(mouseListener);

        SimpleTextAttributes selectionTextAttributes = table.getConfigTextAttributes().getSelection();
        textField.setSelectionColor(selectionTextAttributes.getBgColor());
        textField.setSelectedTextColor(selectionTextAttributes.getFgColor());
    }

    public DatasetEditorTable getTable() {
        return table;
    }

    public void setCell(DatasetEditorModelCell cell) {
        this.cell = cell;
    }

    public void prepareEditor(DatasetEditorModelCell cell) {
        setCell(cell);
        ColumnInfo columnInfo = cell.getColumnInfo();
        DBDataType dataType = columnInfo.getDataType();
        if (dataType.isNative()) {
            BasicDataType basicDataType = dataType.getNativeDataType().getBasicDataType();
            highlight(cell.hasError() ? HIGHLIGHT_TYPE_ERROR : HIGHLIGHT_TYPE_NONE);
            Object userValue = cell.getUserValue();
            if (basicDataType == BasicDataType.LITERAL) {
                String value = (String) userValue;
                setEditable(value == null || value.indexOf('\n') == -1);
            } else if (basicDataType.is(BasicDataType.DATE_TIME, BasicDataType.NUMERIC)) {
                setEditable(true);                    
            } else {
                setEditable(false);
            }
            JTextField textField = getTextField();
            selectText(textField);
        }
    }

    public void setEditable(boolean editable) {
        getTextField().setEditable(editable);
    }

    public void highlight(int type) {
        switch (type) {
            case HIGHLIGHT_TYPE_NONE: getEditorComponent().setBorder(EMPTY_BORDER); break;
            case HIGHLIGHT_TYPE_POPUP: getEditorComponent().setBorder(POPUP_BORDER); break;
            case HIGHLIGHT_TYPE_ERROR: getEditorComponent().setBorder(ERROR_BORDER); break;
        }
    }

    public DatasetEditorModelCell getCell() {
        return cell;
    }

    public boolean isEditable() {
        return getTextField().isEditable();
    }

    protected void selectText(final JTextField textField) {

        if (textField.isEditable()) {
            final String originalText = textField.getText();
            new SimpleLaterInvocator() {
                public void run() {
                    // select all only if the text didn't change
                    if (settings.getGeneralSettings().getSelectContentOnCellEdit().value()) {
                        if (originalText.equals(textField.getText())) {
                            textField.grabFocus();
                            textField.selectAll();
                        }
                    } else {
                        textField.requestFocus();

                        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                        Point textFieldLocation = textField.getLocationOnScreen();
                        int x = (int) Math.max(mouseLocation.getX() - textFieldLocation.getX(), 0);
                        int y = (int) Math.min(Math.max(mouseLocation.getY() - textFieldLocation.getY(), 0), 10);

                        Point location = new Point(x, y);
                        int position = textField.viewToModel(location);
                        textField.setCaretPosition(position);
                    }

                }
            }.start();
        }
    }

    protected boolean isSelected() {
        JTextField textField = getTextField();
        Document document = textField.getDocument();
        return document.getLength() > 0 && textField.getSelectionStart() == 0 && textField.getSelectionEnd() == document.getLength();
    }

    @Override
    public Object getCellEditorValue() {
        return super.getCellEditorValue();
/*
        String stringValue = (String) super.getCellEditorValue();
        return stringValue != null &&
                stringValue.trim().length() > 0 ? stringValue : null;
*/
    }

    /********************************************************
     *                      KeyListener                     *
     ********************************************************/
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {}



    public void keyPressed(KeyEvent e) {
        if (!e.isConsumed()) {
            JTextField textField = getTextField();

            int caretPosition = textField.getCaretPosition();
            if (e.getKeyCode() == 37 ) { // LEFT
                if (isSelected()) {
                    textField.setCaretPosition(0);
                } else  if (caretPosition == 0) {
                    e.consume();
                    cell.editPrevious();
                }
            }
            else if (e.getKeyCode() == 39 ) { // RIGHT
                if (!isSelected() && caretPosition == textField.getDocument().getLength()) {
                    e.consume();
                    cell.editNext();
                }
            }
            else if (e.getKeyCode() == 27 ) { // ESC
                e.consume();
                table.cancelEditing();
            }
        }
    }

    /********************************************************
     *                    MouseListener                     *
     ********************************************************/
    private MouseListener mouseListener = new MouseAdapter() {
        public void mouseReleased(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON3 ) {
                if (cell != null) {
                    table.showPopupMenu(event, cell, cell.getColumnInfo());
                }
            }
        }

        public void mouseClicked(MouseEvent event) {
            if (MouseUtil.isNavigationEvent(event)) {
                DBColumn column = cell.getColumnInfo().getColumn();

                if (column.isForeignKey()) {
                    DatasetFilterInput filterInput = table.getModel().resolveForeignKeyRecord(cell);
                    DatasetEditorManager datasetEditorManager = DatasetEditorManager.getInstance(column.getProject());
                    datasetEditorManager.navigateToRecord(filterInput, event);
                    event.consume();
                }
            }
        }
    };
}