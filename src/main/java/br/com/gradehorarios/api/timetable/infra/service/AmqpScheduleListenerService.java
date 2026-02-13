package br.com.gradehorarios.api.timetable.infra.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gradehorarios.api.shared.domain.service.FileStorageService;
import br.com.gradehorarios.api.shared.infra.config.MessagingConfig;
import br.com.gradehorarios.api.shared.infra.utils.InMemoryMultipartFile;
import br.com.gradehorarios.api.shared.infra.utils.StringUtils;
import br.com.gradehorarios.api.timetable.domain.repository.SolutionRepository;
import br.com.gradehorarios.api.timetable.domain.service.PdfReportService;
import br.com.gradehorarios.api.timetable.domain.service.ScheduleListenerService;
import br.com.gradehorarios.api.timetable.domain.service.SolutionNotificationService;
import br.com.gradehorarios.api.timetable.infra.dto.TimetableResultMessage;
import jakarta.transaction.Transactional;



@Service
public class AmqpScheduleListenerService implements ScheduleListenerService  {
    

    @Autowired
    private FileStorageService storageService;

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
    @Override
    public void recibeveScheduleResponse(TimetableResultMessage message) {

        var solution = this.solutionRepository.findById(message.solutionId()).orElseThrow(() -> new RuntimeException("Solucao nao encontrada"));

        if (message.solution().isPresent()) {
            var solutionDto = message.solution().get();
            try {
                byte[] jsonBytes = objectMapper.writeValueAsBytes(solutionDto);

                var extension = StringUtils.extractExtension(solution.getInputPath());
                var outputName = solution.getInputPath().replace("_input." + extension, "output.json");

                InMemoryMultipartFile file = new InMemoryMultipartFile(
                    "file",
                    outputName,
                    "application/json",
                    jsonBytes
                );
                var outputPath = this.storageService.uploadFile(file, outputName);
                solution.setOutputPath(outputPath);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao processar solucao recebida", e);
            }
            
            

            byte[] teacherPdfBytes = pdfReportService.generateTeacherReport(message.solution().get());

            var extension = StringUtils.extractExtension(solution.getInputPath());
            var teacherPdfName = solution.getInputPath().replace("_input." + extension, "_professores.pdf");
            InMemoryMultipartFile teacherFile = new InMemoryMultipartFile(
                "file",
                teacherPdfName,
                "application/pdf",
                teacherPdfBytes
            );
            String teacherUrl = this.storageService.uploadFile(teacherFile, teacherPdfName);
            solution.setTeacherOutputPath(teacherUrl);


            byte[] classroomPdfBytes = pdfReportService.generateClassroomReport(message.solution().get());
            var classroomPdfName = solution.getInputPath().replace("_input." + extension, "_turmas.pdf");
            
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
