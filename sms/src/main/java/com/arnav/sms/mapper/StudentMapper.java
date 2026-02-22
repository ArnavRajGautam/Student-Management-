package com.arnav.sms.mapper;

import com.arnav.sms.dto.request.StudentRequestDTO;
import com.arnav.sms.dto.response.StudentResponseDTO;
import com.arnav.sms.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    // ResponseDTO mapper == ResponseDTO
    // Entity to Response DTO (( we will send Entity data to user ))

    public StudentResponseDTO toResponseDTO(Student student) {
        if(student == null) {
            return null;
        }
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(student.getId());
        responseDTO.setFirstName(student.getFirstName());
        responseDTO.setLastName(student.getLastName());
        responseDTO.setEmail(student.getEmail());
        responseDTO.setPhone(student.getPhone());
        responseDTO.setDateOfBirth(student.getDateOfBirth());
        responseDTO.setAddress(student.getAddress());
        responseDTO.setEnrollmentDate(student.getEnrollmentDate());
        return responseDTO;
    }

    // Request DTO to Entity (( user will send some data for account creation
    // user se data leke entity me store krenge ( DB )

    public Student toEntity(StudentRequestDTO requestDTO) {
        if(requestDTO == null) {
            return null;
        }
        Student student = new Student();
        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setEmail(requestDTO.getEmail());
        student.setPhone(requestDTO.getPhone());
        student.setDateOfBirth(requestDTO.getDateOfBirth());
        student.setAddress(requestDTO.getAddress());
        return student;

    }

    // for updating User data user will send updated data as req and Student ID ( obj ) to update

    public void UpdateEntityFromDTO(StudentRequestDTO requestDTO, Student student) {
        if(requestDTO == null || student == null) {
            return;
        }
        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setEmail(requestDTO.getEmail());
        student.setPhone(requestDTO.getPhone());
        student.setDateOfBirth(requestDTO.getDateOfBirth());
        student.setAddress(requestDTO.getAddress());

    }


}
