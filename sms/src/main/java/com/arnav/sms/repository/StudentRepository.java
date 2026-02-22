package com.arnav.sms.repository;

import com.arnav.sms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // CRUD - Already available from JpaRepository ✅
    // save(), findById(), findAll(), deleteById(), etc.

    // Custom Methods
    Optional<Student> findByEmail(String email);

    List<Student> findByFirstNameContainingIgnoreCase(String firstName);

    List<Student> findByLastNameContainingIgnoreCase(String lastName);

    boolean existsByEmail(String email);

    List<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);

    // YE QUERY DEKHNA PDEGAAA
    @Query("SELECT s FROM Student s WHERE CONCAT(s.firstName, ' ', s.lastName) LIKE %?1%")
    List<Student> searchByFullName(String name);
}
