package br.com.gradehorarios.gradehorarios.application.service.solution.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.application.dto.solution.TimetableResultMessage;
import br.com.gradehorarios.gradehorarios.application.service.solution.SolutionNotificationService;
import br.com.gradehorarios.gradehorarios.application.service.storage.IStorageService;
import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;
import br.com.gradehorarios.gradehorarios.infra.repository.SolutionRepository;
import br.com.gradehorarios.gradehorarios.infra.utils.InMemoryMultipartFile;
import jakarta.transaction.Transactional;



@Service
public class ScheduleListenerService implements IScheduleListenerService  {
    

    @Autowired
    public IStorageService storageService;

    @Autowired
    public SolutionRepository solutionRepository;

    @Autowired
    public SolutionNotificationService solutionNotificationService;


    @RabbitListener(queues = MessagingConfig.RESULT_QUEUE_NAME)
    @Transactional
    public void recibeveScheduleResponse(TimetableResultMessage message) {

        var solution = this.solutionRepository.findById(message.solutionId()).orElseThrow(() -> new RuntimeException("Solucao nao encontrada"));
        
        var outputName = solution.getInputPath().replace("input", "output");

        InMemoryMultipartFile file = new InMemoryMultipartFile(
            "file",
            outputName,
            "application/json",
            message.solution().toString().getBytes()
        );

        var outputPath = this.storageService.uploadFile(file, outputName);

        solution.setOutputPath(outputPath);
        solution.setSolverStatus(message.solverStatus());
        solution.setDurationMillis(message.durationMilis());
        solution.setErrorMessage(message.errorMessage());
        solution.setWarningMessage(message.warningMessage());
        solution.setModelName(message.modelName());
        
        this.solutionRepository.save(solution);

        this.solutionNotificationService.notifySolutionProcessed(solution);
    }

}
