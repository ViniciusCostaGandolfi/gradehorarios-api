package br.com.gradehorarios.gradehorarios.application.service.solution.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gradehorarios.gradehorarios.application.dto.solution.TimetableResultMessage;
import br.com.gradehorarios.gradehorarios.application.service.solution.PdfReportService;
import br.com.gradehorarios.gradehorarios.application.service.solution.SolutionNotificationService;
import br.com.gradehorarios.gradehorarios.application.service.storage.IStorageService;
import br.com.gradehorarios.gradehorarios.bootstrap.config.MessagingConfig;
import br.com.gradehorarios.gradehorarios.infra.repository.SolutionRepository;
import br.com.gradehorarios.gradehorarios.infra.utils.InMemoryMultipartFile;
import jakarta.transaction.Transactional;



@Service
public class ScheduleListenerService implements IScheduleListenerService  {
    

    @Autowired
    private IStorageService storageService;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private SolutionNotificationService solutionNotificationService;

    @Autowired
    private PdfReportService pdfReportService;

    @Autowired
    private ObjectMapper objectMapper;


    @RabbitListener(queues = MessagingConfig.RESULT_QUEUE_NAME)
    @Transactional
    public void recibeveScheduleResponse(TimetableResultMessage message) {

        var solution = this.solutionRepository.findById(message.solutionId()).orElseThrow(() -> new RuntimeException("Solucao nao encontrada"));

        if (message.solution().isPresent()) {
            var solutionDto = message.solution().get();
            try {
                byte[] jsonBytes = objectMapper.writeValueAsBytes(solutionDto);
                var outputName = solution.getInputPath().replace("input.xlsx", "output.json");

                InMemoryMultipartFile file = new InMemoryMultipartFile(
                    "file",
                    outputName,
                    "application/json",
                    jsonBytes
                );
                var outputPath = this.storageService.uploadFile(file, outputName);
                solution.setOutputPath(outputPath);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar solucao recebida", e);
            }
            
            

            byte[] teacherPdfBytes = pdfReportService.generateTeacherReport(message.solution().get());
            String teacherPdfName = solution.getInputPath().replace("input.xlsx", "_professores.pdf");
            InMemoryMultipartFile teacherFile = new InMemoryMultipartFile(
                "file",
                teacherPdfName,
                "application/pdf",
                teacherPdfBytes
            );
            String teacherUrl = this.storageService.uploadFile(teacherFile, teacherPdfName);
            solution.setTeacherOutputPath(teacherUrl);


            byte[] classroomPdfBytes = pdfReportService.generateClassroomReport(message.solution().get());
            String classroomPdfName = solution.getInputPath().replace("input.xlsx", "_turmas.pdf");
            
            InMemoryMultipartFile classroomFile = new InMemoryMultipartFile(
                "file",
                classroomPdfName,
                "application/pdf",
                classroomPdfBytes
            );
            String classroomUrl = this.storageService.uploadFile(classroomFile, classroomPdfName);
            solution.setClassroomOutputPath(classroomUrl);
        }
        
        
        


        solution.setSolverStatus(message.solverStatus());
        solution.setDurationMillis(message.durationMillis());
        solution.setErrorMessage(message.errorMessage());
        solution.setWarningMessage(message.warningMessage());
        solution.setModelName(message.modelName());
        
        this.solutionRepository.save(solution);

        this.solutionNotificationService.notifySolutionProcessed(solution);
    }

}
