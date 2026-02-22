package com.arnav.sms.mapper;
import com.arnav.sms.dto.response.EnrollmentResponseDTO;
import com.arnav.sms.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    //  ResponseDTO toResponse  --> we will send all the essential details to user via responseDTO

    public EnrollmentResponseDTO toEnrollmentResponseDTO(Enrollment enrollment) {
        if(enrollment == null) {
            return null;
        }
        EnrollmentResponseDTO responseDTO = new EnrollmentResponseDTO();
        responseDTO.setId(enrollment.getId());
        responseDTO.setStudentId(enrollment.getStudent().getId());
        responseDTO.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        responseDTO.setCourseId(enrollment.getCourse().getId());
        responseDTO.setCourseName(enrollment.getCourse().getCourseName());
        responseDTO.setEnrollmentDate(enrollment.getEnrollmentDate());
        responseDTO.setGrade(enrollment.getGrade());
        responseDTO.setStatus(enrollment.getStatus());

        return responseDTO;





    }
}
