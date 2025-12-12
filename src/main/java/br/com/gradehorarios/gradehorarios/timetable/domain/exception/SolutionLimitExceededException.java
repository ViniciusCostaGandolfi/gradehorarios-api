package br.com.gradehorarios.gradehorarios.timetable.domain.exception;

public class SolutionLimitExceededException extends RuntimeException {
    public SolutionLimitExceededException(String message) {
        super(message);
    }
}