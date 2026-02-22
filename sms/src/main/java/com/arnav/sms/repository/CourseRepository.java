package com.arnav.sms.repository;

import com.arnav.sms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // CRUD - Already available ✅

    // Custom Methods
    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByCourseNameContainingIgnoreCase(String courseName);

    List<Course> findByInstructor(String instructor);

    boolean existsByCourseCode(String courseCode);

    List<Course> findByCredits(Integer credits);

    List<Course> findByCreditsGreaterThanEqual(Integer credits);

}
