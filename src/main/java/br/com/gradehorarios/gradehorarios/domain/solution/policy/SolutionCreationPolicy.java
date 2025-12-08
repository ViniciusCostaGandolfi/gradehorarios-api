package br.com.gradehorarios.gradehorarios.domain.solution.policy;

public interface SolutionCreationPolicy {
    void checkQuota(Long userId);
}
