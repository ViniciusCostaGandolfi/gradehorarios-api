package br.com.gradehorarios.api.timetable.domain.exception;

public class SolutionLimitExceededException extends RuntimeException {
    public SolutionLimitExceededException(String message) {
        super(message);
    }
}