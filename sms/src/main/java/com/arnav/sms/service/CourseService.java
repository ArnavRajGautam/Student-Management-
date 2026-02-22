package com.arnav.sms.service;
import com.arnav.sms.dto.request.CourseRequestDTO;
import com.arnav.sms.dto.response.CourseResponseDTO;
import com.arnav.sms.entity.Course;
import com.arnav.sms.exceptions.CourseNotFoundException;
import com.arnav.sms.exceptions.DuplicateCourseException;
import com.arnav.sms.mapper.CourseMapper;
import com.arnav.sms.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }
    // create course
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO) {
        log.info("Creating new course with code: {}", courseRequestDTO.getCourseCode());

        if(courseRepository.existsByCourseCode(courseRequestDTO.getCourseCode())) {
            log.error("Course with code {} already exists", courseRequestDTO.getCourseCode());
            throw new DuplicateCourseException("Course with code " + courseRequestDTO.getCourseCode() + " already exists");
        }
        Course course = courseMapper.toCourseEntity(courseRequestDTO);
        Course savedCourse = courseRepository.save(course);

        log.info("Course created successfully with ID: {}", savedCourse.getCourseCode());
        return courseMapper.toCourseResponseDTO(savedCourse);

    }
    // READ - Get by ID
    public CourseResponseDTO getCourseById(Long id) {
        log.info("Fetching course with ID: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course with ID does not exists: {}", id);
                    return new CourseNotFoundException("Course not found with ID: " + id);
                });

        // ✅ Use Mapper
        return courseMapper.toCourseResponseDTO(course);
    }

    // find by Course Code
    public CourseResponseDTO getByCourseCode(String courseCode) {
        log.info("Getting course with code: {}", courseCode);
        if(!courseRepository.existsByCourseCode(courseCode)) {
            log.error("Course with code {} does not exists", courseCode);
        }
        Course course = courseRepository.findByCourseCode(courseCode).orElseThrow(() -> {
            log.error("Course with code {} does not exists", courseCode);
            return new CourseNotFoundException("Course with code " + courseCode + " does not exists");
        });

        return courseMapper.toCourseResponseDTO(course);
    }

    // Search By Name
    public List<CourseResponseDTO> getCourseByName (String courseName) {
        log.info("Getting course with name: {}", courseName);

        List<Course> courses = courseRepository.findByCourseNameContainingIgnoreCase(courseName);
        log.info("Found {} courses matching: {}", courses.size() , courseName);

        return courses.stream()
                .map(courseMapper::toCourseResponseDTO)
                .collect(Collectors.toList());
    }

    // get all Courses
    public List<CourseResponseDTO> getAllCourses() {
        log.info("fetching all courses");

        List<Course> courses = courseRepository.findAll();
        log.info("found courses: {}", courses.size());

        return courses.stream()
                .map(courseMapper::toCourseResponseDTO)
                .collect(Collectors.toList());

    }

    // Update Course
    public CourseResponseDTO updateCourse(Long id ,CourseRequestDTO courseRequestDTO) {
        log.info("Updating course with code: {}", courseRequestDTO.getCourseCode());

        Course course = courseRepository.findById(id).orElseThrow(()->
                new CourseNotFoundException("Course with code " + id + " does not exists"));

        // check course code duplication
        if(!course.getCourseCode().equals(courseRequestDTO.getCourseCode()) && courseRepository.existsByCourseCode(courseRequestDTO.getCourseCode())) {
            throw new DuplicateCourseException("Course with code " + courseRequestDTO.getCourseCode() + " already exists");
        }
        courseMapper.UpdateCourseEntityFromDTO(courseRequestDTO, course);
        Course updatedCourse = courseRepository.save(course);
        log.info("Course updated successfully with ID: {}", updatedCourse.getCourseCode());
        return courseMapper.toCourseResponseDTO(updatedCourse);

    }

    // DELETE
    public void deleteCourse(Long id) {
        log.info("Deleting course with ID: {}", id);

        if (!courseRepository.existsById(id)) {
            log.error("Course not found with ID: {}", id);
            throw new CourseNotFoundException("Course not found with ID: " + id);
        }

        courseRepository.deleteById(id);
        log.info("Course deleted successfully with ID: {}", id);
    }

    // Get by Instructor
    public List<CourseResponseDTO> getCoursesByInstructor(String instructor) {
        log.info("Fetching courses by instructor: {}", instructor);

        List<Course> courses = courseRepository.findByInstructor(instructor);

        // ✅ Use Mapper
        return courses.stream()
                .map(courseMapper::toCourseResponseDTO)
                .collect(Collectors.toList());
    }


}
