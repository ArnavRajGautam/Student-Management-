package com.arnav.sms.controller;

import com.arnav.sms.dto.api.ApiResponse;
import com.arnav.sms.dto.request.StudentRequestDTO;
import com.arnav.sms.dto.response.StudentResponseDTO;
import com.arnav.sms.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Slf4j
@CrossOrigin(origins = "*")
@Validated
public class StudentController {

    private StudentService studentService;

    @Autowired
    public void StudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Create a new student
     * @param studentRequestDTO Student data
     * @return Created student details
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody StudentRequestDTO studentRequestDTO) {

        log.info("REST request to create student : {}", studentRequestDTO.getEmail());
        StudentResponseDTO studentResponseDTO = studentService.createStudent(studentRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student Created Successfully", studentResponseDTO));
    }

    /**
     * Get student by ID
     * @param Id Student ID
     * @return Student details
     */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(
            @PathVariable("id") Long Id) {

        log.info("REST request to get student by id : {}", Id);
        StudentResponseDTO studentResponseDTO = studentService.getStudentById(Id);

        return ResponseEntity.ok(
                ApiResponse.success("Student Found Successfully", studentResponseDTO));
    }

    /**
     * Get all students
     * @return List of all students
     */

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {

        log.info("REST request to get all students");

        List<StudentResponseDTO> response = studentService.getAllStudents();

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Students retrieved successfully. Total: %d", response.size()),
                        response));
    }


    /**
     * Update student details
     * @param id Student ID
     * @param requestDTO Updated student data
     * @return Updated student details
     */

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        log.info("REST request to update student with ID: {}", id);

        StudentResponseDTO response = studentService.updateStudent(id, requestDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Student updated successfully", response));
    }

    /**
     * Delete student
     * @param id Student ID
     * @return Success message
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {

        log.info("REST request to delete student with ID: {}", id);

        studentService.deleteStudent(id);

        return ResponseEntity.ok(
                ApiResponse.success("Student deleted successfully", null));
    }

    /**
     * Search students by name
     * @param name Search term (first name or last name)
     * @return List of matching students
     */

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> searchStudents(
            @RequestParam @NotBlank(message = "Search name cannot be blank") String name) {

        log.info("REST request to search students with name: {}", name);

        List<StudentResponseDTO> response = studentService.searchStudents(name);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Search completed. Found: %d students", response.size()),
                        response));
    }

    /**
     * Get student by email
     * @param email Student email
     * @return Student details
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByEmail(
            @PathVariable String email) {

        log.info("REST request to get student by email: {}", email);

        StudentResponseDTO response = studentService.getStudentByEmail(email);

        return ResponseEntity.ok(
                ApiResponse.success("Student retrieved successfully", response));
    }
}
