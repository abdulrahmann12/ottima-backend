package com.ottima.finishing_tracking.exception;
import com.ottima.finishing_tracking.common.messages.Messages;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super(Messages.PROJECT_NOT_FOUND);
    }
}