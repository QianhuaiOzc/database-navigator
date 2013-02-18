package com.dci.intellij.dbn.debugger.breakpoint;

import com.dci.intellij.dbn.vfs.DatabaseEditableObjectFile;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.impl.breakpoints.XBreakpointManagerImpl;
import com.intellij.xdebugger.impl.breakpoints.XLineBreakpointImpl;

/**
 * WORKAROUND: Breakpoints do not seem to be registered properly in the XLineBreakpointManager.
 * This way the breakpoints forName updated as soon as the file is opened.
 */
public class BreakpointUpdaterFileEditorListener implements FileEditorManagerListener{
    public void fileOpened(FileEditorManager source, VirtualFile file) {
        if (file instanceof DatabaseEditableObjectFile) {
            DatabaseEditableObjectFile databaseFile = (DatabaseEditableObjectFile) file;
            XBreakpointManagerImpl breakpointManager = (XBreakpointManagerImpl) XDebuggerManager.getInstance(source.getProject()).getBreakpointManager();
            for (XBreakpoint breakpoint : breakpointManager.getAllBreakpoints()) {
                if (breakpoint instanceof XLineBreakpoint) {
                    XLineBreakpoint lineBreakpoint = (XLineBreakpoint) breakpoint;
                    lineBreakpoint.putUserData(DBProgramBreakpointHandler.BREAKPOINT_ID_KEY, null);
                    DatabaseEditableObjectFile breakpointDatabaseFile = DBProgramBreakpointHandler.getDatabaseFile(lineBreakpoint);
                    if (databaseFile == breakpointDatabaseFile) {
                        breakpointManager.getLineBreakpointManager().registerBreakpoint((XLineBreakpointImpl) lineBreakpoint, true);
                    }
                }
            }
        }
    }

    public void fileClosed(FileEditorManager source, VirtualFile file) {
    }

    public void selectionChanged(FileEditorManagerEvent event) {
    }
}
