package com.arnav.sms.exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

    @RestControllerAdvice
    @Slf4j
    public class GlobalExceptionHandler {

        // this call handles Student Not Found Exception ,
        // the return is ErrorResponseWrapper ( not string , obj ) as we will return errorLog when Exception comes
        // we called ERW constructor assigned errorLog values and returned to the caller
        // via ResponseEntity we returned ErrResWrapper class object and Status code


        // =========== Student Not Found =============
        @ExceptionHandler(StudentNotFoundException.class)
        public ResponseEntity<ErrorResponseWrapper> handleStudentNotFoundException(StudentNotFoundException ex , WebRequest request) {

            log.error("StudentNotFoundException: {}" , ex.getMessage());
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(
                    LocalDateTime.now() ,
                    HttpStatus.NOT_FOUND.value(), "Not found",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );
            return new ResponseEntity<>(errorResponseWrapper, HttpStatus.NOT_FOUND);

        }

        // Course Not Found
        @ExceptionHandler(CourseNotFoundException.class)
        public ResponseEntity<ErrorResponseWrapper> handleCourseNotFoundException(CourseNotFoundException ex , WebRequest request) {

            log.error("CourseNotFoundException: {}" , ex.getMessage());
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(
                    LocalDateTime.now() ,
                    HttpStatus.NOT_FOUND.value() , "Not Found" ,
                    ex.getMessage() ,
                    request.getDescription(false).replace("uri=", "")
            );
            return new ResponseEntity<>(errorResponseWrapper, HttpStatus.NOT_FOUND);
        }

        // Enrollment Not Found
        @ExceptionHandler(EnrollmentException.class)
        public ResponseEntity<ErrorResponseWrapper> handleEnrollmentException(EnrollmentException ex , WebRequest request) {
            log.error("EnrollmentNotFoundException: {}" , ex.getMessage());
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(
                    LocalDateTime.now() ,
                    HttpStatus.NOT_FOUND.value() , "Not Found" ,
                    ex.getMessage() ,
                    request.getDescription(false).replace("uri=","")
            );
            return new ResponseEntity<>(errorResponseWrapper, HttpStatus.NOT_FOUND);
        }

        // Duplicate Email
        @ExceptionHandler(DuplicateEmailException.class)
        public ResponseEntity<ErrorResponseWrapper> handleDuplicateEmailException(
                DuplicateEmailException ex, WebRequest request) {

            log.error("DuplicateEmailException: {}", ex.getMessage());

            ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(), "Conflict",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // Duplicate Course
        @ExceptionHandler(DuplicateCourseException.class)
        public ResponseEntity<ErrorResponseWrapper> handleDuplicateCourseException(
                DuplicateCourseException ex, WebRequest request) {

            log.error("DuplicateCourseException: {}", ex.getMessage());

            ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(), "Conflict",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // Duplicate Enrollment
        @ExceptionHandler(DuplicateEnrollmentException.class)
        public ResponseEntity<ErrorResponseWrapper> handleDuplicateEnrollmentException(
                DuplicateEnrollmentException ex, WebRequest request) {

            log.error("DuplicateEnrollmentException: {}", ex.getMessage());

            ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(), "Conflict",
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "")
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        // Validation Exceptions

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public  ResponseEntity<ErrorResponseWrapper> handleMethodArgumentNotValidException(
                MethodArgumentNotValidException ex , WebRequest request){

            log.error("MethodArgumentNotValidException: {}" , ex.getMessage());

            Map<String,String> validationErrors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach((error) -> {
                validationErrors.put(error.getField(),error.getDefaultMessage());

            });

            ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(
                    LocalDateTime.now() ,
                    HttpStatus.BAD_REQUEST.value(), "validation Failed" ,
                    "One or more Fields have validation errors" ,
                    request.getDescription(false).replace("uri=","")

            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Generic Exception
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseWrapper> handleException(Exception ex , WebRequest request) {
            log.error("Unexpected Error Occurred Exception: {}" , ex.getMessage());
            ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(
                    LocalDateTime.now() ,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server error" ,
                    "An Unexpected Error Occurred. Please try again later.",
                    request.getDescription(false).replace("uri=","")
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


