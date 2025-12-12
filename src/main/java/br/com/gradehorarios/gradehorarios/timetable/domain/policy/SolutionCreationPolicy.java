package br.com.gradehorarios.gradehorarios.timetable.domain.policy;

public interface SolutionCreationPolicy {
    void checkQuota(Long userId);
}
