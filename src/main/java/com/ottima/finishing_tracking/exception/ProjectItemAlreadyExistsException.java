package com.ottima.finishing_tracking.exception;
import com.ottima.finishing_tracking.common.messages.Messages;

public class ProjectItemAlreadyExistsException extends RuntimeException {
    public ProjectItemAlreadyExistsException() {
        super(Messages.PROJECT_ITEM_ALREADY_EXISTS);
    }
}