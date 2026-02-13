package br.com.gradehorarios.api.timetable.domain.model;

public enum SolverStatus {

    OPTIMAL,
    FEASIBLE,
    INFEASIBLE,
    ERROR,
    RUNNING,
    TIMEOUT,
    PENDING,
    UNKNOWN;

}
