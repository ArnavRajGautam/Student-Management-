package com.arnav.sms.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false , unique = true)
    private String email;

    @Column(length = 10, nullable = false)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 200)
    private String address;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    // Enrollments : one student -> many Enrollments

    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST , orphanRemoval = true)

    private List<Enrollment> enrollment = new ArrayList<>();

    // logic to set CurrentDate when spring save Student data
    // sets Automatic due to Annotation
    @PrePersist
    public void setEnrollmentDate() {
        if (this.enrollmentDate == null) {
            this.enrollmentDate = LocalDate.now();
        }
    }


}
