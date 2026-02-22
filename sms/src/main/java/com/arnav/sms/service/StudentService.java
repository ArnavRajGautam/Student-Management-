package com.arnav.sms.service;

import com.arnav.sms.dto.request.StudentRequestDTO;
import com.arnav.sms.dto.response.StudentResponseDTO;
import com.arnav.sms.entity.Student;
import com.arnav.sms.exceptions.DuplicateEmailException;
import com.arnav.sms.exceptions.StudentNotFoundException;
import com.arnav.sms.mapper.StudentMapper;
import com.arnav.sms.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    // Create Student
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {

        log.info("Creating new Student with email : {}", studentRequestDTO.getEmail());

        if(studentRepository.findByEmail(studentRequestDTO.getEmail()).isPresent()){
            log.error("Student with email {} already exists", studentRequestDTO.getEmail());
            throw new DuplicateEmailException("Student with email " + studentRequestDTO.getEmail() + " already exists");
        }
        Student student = studentMapper.toEntity(studentRequestDTO);
        Student savedStudent = studentRepository.save(student);
        log.info("Student Created Successfully with email : {}", savedStudent.getEmail());
        log.info("Your Student ID is : {}", savedStudent.getId());

        return studentMapper.toResponseDTO(savedStudent);
    }

    // READ - Get by ID
    public StudentResponseDTO getStudentById(Long studentId) {

        log.info("Getting Student with id : {}", studentId);

        Student student = studentRepository.findById(studentId).orElseThrow(()->{
            log.error("Student with id {} not found", studentId);
            return new StudentNotFoundException("Student with id " + studentId + " not found");
        });

        return studentMapper.toResponseDTO(student);
    }

    // find all students
    public List<StudentResponseDTO> getAllStudents() {

        log.info("Getting All Students");

        List<Student> students = studentRepository.findAll();
        log.info("Found {} Students", students.size());

        return students.stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update Students
    public StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO studentRequestDTO) {
        log.info("Updating Student with id : {}", studentId);
        Student student = studentRepository.findById(studentId).orElseThrow(
                ()->new StudentNotFoundException("Student with id " + studentId + " not found")
        );
        if(!student.getEmail().equals(studentRequestDTO.getEmail()) && studentRepository.existsByEmail(studentRequestDTO.getEmail())){
            throw new DuplicateEmailException("Student with email " + studentRequestDTO.getEmail() + " already exists");
        }
        studentMapper.UpdateEntityFromDTO(studentRequestDTO, student);
        Student updatedStudent = studentRepository.save(student);
        log.info("Student Updated Successfully with id : {}", updatedStudent.getId());
        return studentMapper.toResponseDTO(updatedStudent);
    }

    // DELETE
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);

        if (!studentRepository.existsById(id)) {
            log.error("Student not found with ID: {}", id);
            throw new StudentNotFoundException("Student not found with ID: " + id);
        }

        studentRepository.deleteById(id);
        log.info("Student deleted successfully with ID: {}", id);
    }

    // Search by Name
    public List<StudentResponseDTO> searchStudents(String name) {
        log.info("Searching students with name : {}", name);

        List<Student> students = studentRepository.searchByFullName(name);
        log.info("Found {} Students matching: {}", students.size() , name);

        return students.stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Get By Email
    public StudentResponseDTO getStudentByEmail(String email) {
        log.info("Getting Student with email : {}", email);
        Student student = studentRepository.findByEmail(email).orElseThrow(( ) ->
                new StudentNotFoundException("Student with email " + email + " not found")
        );
        return studentMapper.toResponseDTO(student);
    }


}
