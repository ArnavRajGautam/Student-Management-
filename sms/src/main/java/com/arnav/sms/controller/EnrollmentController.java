package com.arnav.sms.controller;

import com.arnav.sms.dto.api.ApiResponse;
import com.arnav.sms.dto.request.EnrollmentRequestDTO;
import com.arnav.sms.dto.response.EnrollmentResponseDTO;
import com.arnav.sms.entity.EnrollmentStatus;
import com.arnav.sms.service.EnrollmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@Slf4j
@CrossOrigin(origins = "*")
@Validated
public class EnrollmentController {

    private EnrollmentService enrollmentService;
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }


    /**
     * Enroll a student in a course
     * @param requestDTO Enrollment data (studentId, courseId)
     * @return Created enrollment details
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> enrollStudent(
            @Valid @RequestBody EnrollmentRequestDTO requestDTO) {

        log.info("REST request to enroll student ID: {} in course ID: {}",
                requestDTO.getStudentId(), requestDTO.getCourseId());

        EnrollmentResponseDTO response = enrollmentService.enrollStudent(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student enrolled successfully", response));
    }

    /**
     * Get enrollment by ID
     * @param id Enrollment ID
     * @return Enrollment details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getEnrollmentById(
            @PathVariable Long id) {

        log.info("REST request to get enrollment by ID: {}", id);

        EnrollmentResponseDTO response = enrollmentService.getEnrollmentById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Enrollment retrieved successfully", response));
    }

    /**
     * Get all enrollments
     * @return List of all enrollments
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getAllEnrollments() {

        log.info("REST request to get all enrollments");

        List<EnrollmentResponseDTO> response = enrollmentService.getAllEnrollments();

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Enrollments retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Get all enrollments for a specific student
     * @param studentId Student ID
     * @return List of enrollments for the student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {

        log.info("REST request to get enrollments for student ID: {}", studentId);

        List<EnrollmentResponseDTO> response = enrollmentService.getEnrollmentsByStudent(studentId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Enrollments retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Get all enrollments for a specific course
     * @param courseId Course ID
     * @return List of enrollments for the course
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {

        log.info("REST request to get enrollments for course ID: {}", courseId);

        List<EnrollmentResponseDTO> response = enrollmentService.getEnrollmentsByCourse(courseId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Enrollments retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Get active enrollments for a specific student
     * @param studentId Student ID
     * @return List of active enrollments
     */

    @GetMapping("/student/{studentId}/active")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getActiveEnrollmentsByStudent(
            @PathVariable Long studentId) {

        log.info("REST request to get active enrollments for student ID: {}", studentId);

        List<EnrollmentResponseDTO> response =
                enrollmentService.getActiveEnrollmentsByStudent(studentId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Active enrollments retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Get enrollments by status
     * @param status Enrollment status (ACTIVE, COMPLETED, DROPPED)
     * @return List of enrollments with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStatus(
            @PathVariable EnrollmentStatus status) {

        log.info("REST request to get enrollments with status: {}", status);

        List<EnrollmentResponseDTO> response = enrollmentService.getEnrollmentsByStatus(status);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Enrollments retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Update grade for an enrollment
     * @param id Enrollment ID
     * @param grade Grade (A+, A, B+, B, C+, C, D, F)
     * @return Updated enrollment details
     */
    @PatchMapping("/{id}/grade")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> updateGrade(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Grade cannot be blank") String grade) {

        log.info("REST request to update grade for enrollment ID: {} to {}", id, grade);

        EnrollmentResponseDTO response = enrollmentService.updateGrade(id, grade);

        return ResponseEntity.ok(
                ApiResponse.success("Grade updated successfully", response));
    }

    /**
     * Update status for an enrollment
     * @param id Enrollment ID
     * @param status Status (ACTIVE, COMPLETED, DROPPED)
     * @return Updated enrollment details
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {

        log.info("REST request to update status for enrollment ID: {} to {}", id, status);

        EnrollmentResponseDTO response = enrollmentService.updateStatus(id, status);

        return ResponseEntity.ok(
                ApiResponse.success("Status updated successfully", response));
    }

    /**
     * Delete enrollment
     * @param id Enrollment ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {

        log.info("REST request to delete enrollment with ID: {}", id);

        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.ok(
                ApiResponse.success("Enrollment deleted successfully", null));
    }

    /**
     * Count total enrollments for a student
     * @param studentId Student ID
     * @return Total count of enrollments
     */
    @GetMapping("/student/{studentId}/count")
    public ResponseEntity<ApiResponse<Long>> countEnrollmentsByStudent(
            @PathVariable Long studentId) {

        log.info("REST request to count enrollments for student ID: {}", studentId);

        Long count = enrollmentService.countEnrollmentsByStudent(studentId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Student has %d enrollments", count),
                        count));
    }

    /**
     * Count total enrollments for a course
     * @param courseId Course ID
     * @return Total count of enrollments
     */
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<ApiResponse<Long>> countEnrollmentsByCourse(
            @PathVariable Long courseId) {

        log.info("REST request to count enrollments for course ID: {}", courseId);

        Long count = enrollmentService.countEnrollmentsByCourse(courseId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Course has %d enrollments", count),
                        count));
    }

}
