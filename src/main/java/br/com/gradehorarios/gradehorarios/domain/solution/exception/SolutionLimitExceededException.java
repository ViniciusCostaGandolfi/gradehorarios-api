package br.com.gradehorarios.gradehorarios.domain.solution.exception;

public class SolutionLimitExceededException extends RuntimeException {
    public SolutionLimitExceededException(String message) {
        super(message);
    }
}