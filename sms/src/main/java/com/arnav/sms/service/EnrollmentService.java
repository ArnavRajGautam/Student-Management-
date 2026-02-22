package com.arnav.sms.service;

import com.arnav.sms.dto.request.EnrollmentRequestDTO;
import com.arnav.sms.dto.response.EnrollmentResponseDTO;
import com.arnav.sms.entity.Course;
import com.arnav.sms.entity.Enrollment;
import com.arnav.sms.entity.EnrollmentStatus;
import com.arnav.sms.entity.Student;
import com.arnav.sms.exceptions.CourseNotFoundException;
import com.arnav.sms.exceptions.DuplicateEnrollmentException;
import com.arnav.sms.exceptions.EnrollmentException;
import com.arnav.sms.exceptions.StudentNotFoundException;
import com.arnav.sms.mapper.EnrollmentMapper;
import com.arnav.sms.repository.CourseRepository;
import com.arnav.sms.repository.EnrollmentRepository;
import com.arnav.sms.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;  // ✅ Add Mapper

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public EnrollmentResponseDTO enrollStudent(EnrollmentRequestDTO enrollmentRequestDTO) {
        log.info("Enrollment request received for ");
        log.info("Enrolling student ID: {} to course ID: {}",
                enrollmentRequestDTO.getStudentId(), enrollmentRequestDTO.getCourseId());

        // check if student exists
        Student student = studentRepository.findById(enrollmentRequestDTO.getStudentId()).orElseThrow(() ->{
            log.error("Student ID: {} not found", enrollmentRequestDTO.getStudentId());
            return new StudentNotFoundException( "Student not found with ID: " + enrollmentRequestDTO.getStudentId());
        });

        // Check if course exists
        Course course =  courseRepository.findById(enrollmentRequestDTO.getCourseId()).orElseThrow(()->{
            log.error("Course ID: {} not found", enrollmentRequestDTO.getCourseId());
            return new CourseNotFoundException("Course not found with ID: " + enrollmentRequestDTO.getCourseId());
        });

        if(enrollmentRepository.existsByStudentIdAndCourseId(enrollmentRequestDTO.getStudentId() , enrollmentRequestDTO.getCourseId())){
            log.error("Student already enrolled in this course");
            throw new DuplicateEnrollmentException("Student is already enrolled in this course");

        }
        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Enrollment created successfully with ID: {}", savedEnrollment.getId());

        // ✅ Use Mapper
        return enrollmentMapper.toEnrollmentResponseDTO(savedEnrollment);

    }

    // READ - Get by ID
    public EnrollmentResponseDTO getEnrollmentById(Long id) {
        log.info("Fetching enrollment with ID: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Enrollment not found with ID: {}", id);
                    return new EnrollmentException(
                            "Enrollment not found with ID: " + id);
                });

        // ✅ Use Mapper
        return enrollmentMapper.toEnrollmentResponseDTO(enrollment);
    }

    // READ - Get All
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        log.info("Fetching all enrollments");

        List<Enrollment> enrollments = enrollmentRepository.findAll();
        log.info("Found {} enrollments", enrollments.size());

        // ✅ Use Mapper
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    // Get Enrollments by Student
    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(Long studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);

        // Check if student exists
        if (!studentRepository.existsById(studentId)) {
            log.error("Student not found with ID: {}", studentId);
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        // get all Enrollments Courses ID by StudentID
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        log.info("Found {} enrollments for student ID: {}", enrollments.size(), studentId);

        // ✅ Use Mapper
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    // Get Enrollments by Course
    public List<EnrollmentResponseDTO> getEnrollmentsByCourse(Long courseId) {
        log.info("Fetching enrollments for course ID: {}", courseId);

        // Check if course exists
        if (!courseRepository.existsById(courseId)) {
            log.error("Course not found with ID: {}", courseId);
            throw new CourseNotFoundException("Course not found with ID: " + courseId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        log.info("Found {} enrollments for course ID: {}", enrollments.size(), courseId);

        // ✅ Use Mapper
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    // UPDATE - Update Grade
    public EnrollmentResponseDTO updateGrade(Long id, String grade) {
        log.info("Updating grade for enrollment ID: {} to {}", id, grade);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Enrollment with ID does not exits: {}", id);
                    return new EnrollmentException(
                            "Enrollment not found with ID: " + id);
                });

        enrollment.setGrade(grade);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        log.info("Grade updated successfully for enrollment ID: {}", id);

        // ✅ Use Mapper
        return enrollmentMapper.toEnrollmentResponseDTO(updatedEnrollment);
    }

    // UPDATE - Update Status
    public EnrollmentResponseDTO updateStatus(Long id, EnrollmentStatus status) {
        log.info("Updating status for enrollment ID: {} to {}", id, status);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Enrollment not found with ID : {}", id);
                    return new EnrollmentException(
                            "Enrollment not found with ID: " + id);
                });

        enrollment.setStatus(status);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        log.info("Status updated successfully for enrollment ID: {}", id);

        // ✅ Use Mapper
        return enrollmentMapper.toEnrollmentResponseDTO(updatedEnrollment);
    }

    // DELETE
    public void deleteEnrollment(Long id) {
        log.info("Deleting enrollment with ID: {}", id);

        if (!enrollmentRepository.existsById(id)) {
            log.error(" Enrollment not found with ID: {}", id);
            throw new EnrollmentException("Enrollment not found with ID: " + id);
        }

        enrollmentRepository.deleteById(id);
        log.info("Enrollment deleted successfully with ID: {}", id);
    }

    // Get Student's Active Enrollments
    public List<EnrollmentResponseDTO> getActiveEnrollmentsByStudent(Long studentId) {
        log.info("Fetching active enrollments for student ID: {}", studentId);

        // Check if student exists
        if (!studentRepository.existsById(studentId)) {
            log.error("Student not found with ID : {}", studentId);
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }

        List<Enrollment> enrollments = enrollmentRepository
                .findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE);

        log.info("Found {} active enrollments for student ID : {}",
                enrollments.size(), studentId);

        // ✅ Use Mapper
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    // Get Enrollments by Status
    public List<EnrollmentResponseDTO> getEnrollmentsByStatus(EnrollmentStatus status) {
        log.info("Fetching enrollments with status: {}", status);

        List<Enrollment> enrollments = enrollmentRepository.findByStatus(status);
        log.info("Found {} enrollments with status: {}", enrollments.size(), status);

        // ✅ Use Mapper
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    // Count Enrollments by Student
    public Long countEnrollmentsByStudent(Long studentId) {
        log.info("Counting enrollments for student ID: {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }

        Long count = enrollmentRepository.countEnrollmentsByStudentId(studentId);
        log.info("Student ID {} has {} enrollments", studentId, count);

        return count;
    }

    // Count Enrollments by Course
    public Long countEnrollmentsByCourse(Long courseId) {
        log.info("Counting enrollments for course ID: {}", courseId);

        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with ID: " + courseId);
        }

        Long count = enrollmentRepository.countEnrollmentsByCourseId(courseId);
        log.info("Course ID {} has {} enrollments", courseId, count);

        return count;
    }



}
