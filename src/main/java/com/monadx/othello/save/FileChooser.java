package com.monadx.othello.save;

import java.awt.FileDialog;
import java.awt.Frame;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChooser {
    private final Action action;
    private final String filenameFilter;

    public FileChooser(Action action, String filenameFilter) {
        this.action = action;
        this.filenameFilter = filenameFilter;
    }

    public String choose() {
        FileDialog dialog = new FileDialog((Frame) null, "", action.getDialogMode());

        if (action == Action.SAVE) {
            dialog.setFile("Untitled" + filenameFilter);
        } else {
            dialog.setFilenameFilter((dir, name) -> name.endsWith(filenameFilter));
        }

        dialog.setVisible(true);

        String filename = dialog.getFile();
        if (filename == null) {
            return null;
        }
        Path path = Paths.get(dialog.getDirectory(), filename);
        return path.toString();
    }

    public enum Action {
        SAVE(FileDialog.SAVE),
        OPEN(FileDialog.LOAD);

        private final int dialogMode;

        Action(int dialogMode) {
            this.dialogMode = dialogMode;
        }

        public int getDialogMode() {
            return dialogMode;
        }
    }
}
