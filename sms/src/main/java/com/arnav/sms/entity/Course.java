package com.arnav.sms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name" , nullable = false ,length = 100)
    private String courseName;

    @Column(name = " course_code" , nullable = false , unique = true , length = 20)
    private String courseCode;

    @Column(nullable = false)
    private Integer credits;

    @Column(length = 100)
    private String instructor;

    @Column(length = 500)
    private String description;

    // One course : Many Enrollments
    @OneToMany( mappedBy = "course" , cascade = CascadeType.PERSIST ,
            orphanRemoval = true
    )
    private List<Enrollment> enrollments = new ArrayList<>();




}
