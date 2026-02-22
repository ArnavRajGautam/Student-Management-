package com.arnav.sms.repository;

import com.arnav.sms.entity.Enrollment;
import com.arnav.sms.entity.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // CRUD - Already available ✅

    // Custom Methods
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);


    // ye query dekhni pdegi
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = ?1 AND e.status = ?2")
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = ?1")
    Long countEnrollmentsByStudentId(Long studentId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = ?1")
    Long countEnrollmentsByCourseId(Long courseId);
}

