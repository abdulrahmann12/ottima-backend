package com.ottima.finishing_tracking.exception;

import com.ottima.finishing_tracking.common.messages.Messages;

public class ProjectItemMismatchException extends RuntimeException {
    public ProjectItemMismatchException() {
        super("Project item does not belong to the same project.");
    }
}

