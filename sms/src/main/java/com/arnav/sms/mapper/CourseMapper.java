package com.arnav.sms.mapper;
import com.arnav.sms.dto.request.CourseRequestDTO;
import com.arnav.sms.dto.response.CourseResponseDTO;
import com.arnav.sms.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    // Entity to ResponseDTO
    // Return all essential details to user via ResponseDTO

    public CourseResponseDTO toCourseResponseDTO(Course course) {
        if (course == null) {
            return null;
        }
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        courseResponseDTO.setId(course.getId());
        courseResponseDTO.setCourseName(course.getCourseName());
        courseResponseDTO.setCourseCode(course.getCourseCode());
        courseResponseDTO.setCredits(course.getCredits());
        courseResponseDTO.setInstructor(course.getInstructor());
        courseResponseDTO.setDescription(course.getDescription());

        return courseResponseDTO;
    }

    public Course toCourseEntity(CourseRequestDTO courseRequestDTO) {
        if (courseRequestDTO == null) {
            return null;
        }
        Course course = new Course();
        course.setCourseName(courseRequestDTO.getCourseName());
        course.setCourseCode(courseRequestDTO.getCourseCode());
        course.setCredits(courseRequestDTO.getCredits());
        course.setInstructor(courseRequestDTO.getInstructor());
        course.setDescription(courseRequestDTO.getDescription());

        return course;
    }

    public void UpdateCourseEntityFromDTO(CourseRequestDTO courseRequestDTO , Course course) {
        if (courseRequestDTO == null ||  course == null) {
            return;
        }
        course.setCourseName(courseRequestDTO.getCourseName());
        course.setCourseCode(courseRequestDTO.getCourseCode());
        course.setCredits(courseRequestDTO.getCredits());
        course.setInstructor(courseRequestDTO.getInstructor());
        course.setDescription(courseRequestDTO.getDescription());


    }
}
