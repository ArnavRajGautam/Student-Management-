package com.arnav.sms.controller;

import com.arnav.sms.dto.api.ApiResponse;
import com.arnav.sms.dto.request.CourseRequestDTO;
import com.arnav.sms.dto.response.CourseResponseDTO;
import com.arnav.sms.service.CourseService;
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
@RequestMapping("/api/courses")
@Slf4j
@CrossOrigin(origins = "*")
@Validated
public class CourseController {

    @Autowired
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Create a new course
     * @param requestDTO Course data
     * @return Created course details
     */

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(
            @Valid @RequestBody CourseRequestDTO requestDTO) {

        log.info("REST request to create course: {}", requestDTO.getCourseCode());

        CourseResponseDTO response = courseService.createCourse(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", response));
    }


    /**
     * Get course by ID
     * @param id Course ID
     * @return Course details
     */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(
            @PathVariable Long id) {

        log.info("REST request to get course by ID: {}", id);

        CourseResponseDTO response = courseService.getCourseById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Course retrieved successfully", response));
    }

    /**
     * Get all courses
     * @return List of all courses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {

        log.info("REST request to get all courses");
        List<CourseResponseDTO> response = courseService.getAllCourses();
        return ResponseEntity.ok(
                ApiResponse.success(String.format("Courses retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Update course details
     * @param id Course ID
     * @param requestDTO Updated course data
     * @return Updated course details
     */

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDTO requestDTO) {

        log.info("REST request to update course with ID: {}", id);

        CourseResponseDTO response = courseService.updateCourse(id, requestDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Course updated successfully", response));
    }

    /**
     * Delete course
     * @param id Course ID
     * @return Success message
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {

        log.info("REST request to delete course with ID: {}", id);

        courseService.deleteCourse(id);

        return ResponseEntity.ok(
                ApiResponse.success("Course deleted successfully", null));
    }

    /**
     * Search courses by name
     * @param name Search term (course name)
     * @return List of matching courses
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> searchCourses(
            @RequestParam @NotBlank(message = "Search name cannot be blank") String name) {

        log.info("REST request to search courses with name: {}", name);

        List<CourseResponseDTO> response = courseService.getCourseByName(name);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Search completed. Found: %d courses", response.size()),
                        response));
    }

    /**
     * Get course by course code
     * @param courseCode Course code (e.g., CS101)
     * @return Course details
     */

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseByCourseCode(
            @PathVariable String courseCode) {

        log.info("REST request to get course by code: {}", courseCode);

        CourseResponseDTO response = courseService.getByCourseCode(courseCode);

        return ResponseEntity.ok(
                ApiResponse.success("Course retrieved successfully", response));
    }

    /**
     * Get courses by instructor name
     * @param instructor Instructor name
     * @return List of courses taught by the instructor
     */
    @GetMapping("/instructor/{instructor}")
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getCoursesByInstructor(
            @PathVariable String instructor) {

        log.info("REST request to get courses by instructor: {}", instructor);

        List<CourseResponseDTO> response = courseService.getCoursesByInstructor(instructor);

        return ResponseEntity.ok(
                ApiResponse.success(
                        String.format("Courses retrieved successfully. Total: %d", response.size()),
                        response));
    }

    /**
     * Get courses by credits
     * @param credits Number of credits
     * @return List of courses with specified credits
     */
    @GetMapping("/credits/{credits}")
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getCoursesByCredits(
            @PathVariable Integer credits) {

        log.info("REST request to get courses with credits: {}", credits);

        // You'll need to add this method in CourseService
        // List<CourseResponseDTO> response = courseService.getCoursesByCredits(credits);

        return ResponseEntity.ok(
                ApiResponse.success("Feature coming soon", null));
    }
}
