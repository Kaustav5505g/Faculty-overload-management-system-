package com.system.service;

public class WorkloadExceededException extends Exception {
    public WorkloadExceededException(String message) {
        super(message);
    }
}