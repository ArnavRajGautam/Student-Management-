package com.arnav.sms.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 100, message = "Course name must be between 3 and 100 characters")
    private String courseName;

    @NotBlank(message = "Course code is required")
    @Pattern(regexp = "^[A-Z]{2,4}[0-9]{3}$",
            message = "Course code format: 2-4 uppercase letters followed by 3 digits (e.g., CS101)")
    private String courseCode;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits cannot exceed 6")
    private Integer credits;

    @NotBlank(message = "Instructor name is required")
    @Size(max = 100, message = "Instructor name cannot exceed 100 characters")
    private String instructor;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;



}
