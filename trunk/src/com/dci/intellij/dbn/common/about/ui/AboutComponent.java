package com.dci.intellij.dbn.common.about.ui;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutComponent extends DBNFormImpl{
    private JPanel mainPanel;
    private JLabel splashLabel;
    private JLabel donateLabel;
    private JLabel downloadPageLinkLabel;
    private JLabel supportPageLinkLabel;
    private JLabel requestTrackerPageLinkLabel;

    public AboutComponent() {
        Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        splashLabel.setIcon(Icons.DATABASE_NAVIGATOR);
        splashLabel.setText("");

        donateLabel.setIcon(Icons.DONATE_DISABLED);
        donateLabel.setText("");
        donateLabel.setCursor(handCursor);
        donateLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BrowserUtil.browse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=3QAPZFCCARA4J");
            }

            public void mouseEntered(MouseEvent e) {
                donateLabel.setIcon(Icons.DONATE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                donateLabel.setIcon(Icons.DONATE_DISABLED);
            }
        });

        downloadPageLinkLabel.setForeground(CodeInsightColors.HYPERLINK_ATTRIBUTES.getDefaultAttributes().getForegroundColor());
        downloadPageLinkLabel.setCursor(handCursor);
        downloadPageLinkLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BrowserUtil.browse("http://plugins.jetbrains.com/plugin/?id=1800");
            }
        });

        supportPageLinkLabel.setForeground(CodeInsightColors.HYPERLINK_ATTRIBUTES.getDefaultAttributes().getForegroundColor());
        supportPageLinkLabel.setCursor(handCursor);
        supportPageLinkLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BrowserUtil.browse("http://confluence.jetbrains.com/display/CONTEST/Database+Navigator");
            }
        });

        requestTrackerPageLinkLabel.setForeground(CodeInsightColors.HYPERLINK_ATTRIBUTES.getDefaultAttributes().getForegroundColor());
        requestTrackerPageLinkLabel.setCursor(handCursor);
        requestTrackerPageLinkLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BrowserUtil.browse("http://dci.myjetbrains.com/youtrack/issues");
            }
        });

    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public void showPopup(Project project) {
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(mainPanel, null);
        JBPopup popup = popupBuilder.createPopup();
        popup.showCenteredInCurrentWindow(project);
    }
}
