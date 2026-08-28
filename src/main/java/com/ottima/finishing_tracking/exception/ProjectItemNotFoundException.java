package com.ottima.finishing_tracking.exception;
import com.ottima.finishing_tracking.common.messages.Messages;

public class ProjectItemNotFoundException extends RuntimeException {
    public ProjectItemNotFoundException() {
        super(Messages.PROJECT_ITEM_NOT_FOUND);
    }
}