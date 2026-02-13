package br.com.gradehorarios.api.timetable.domain.policy;

public interface SolutionCreationPolicy {
    void checkQuota(Long userId);
}
