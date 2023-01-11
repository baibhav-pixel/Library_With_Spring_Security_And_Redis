package com.example.library.Controllers;

import com.example.library.Models.Student;
import com.example.library.Models.User;
import com.example.library.Requests.PlaceRequest;
import com.example.library.Requests.StudentCreateRequest;
import com.example.library.Services.StudentService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    //Anyone can create student -- No security for this API
    //Permit ALL
    @PostMapping("/student")
    public Student createStudent(@Valid @RequestBody StudentCreateRequest studentCreateRequest){
        return studentService.createStudent(studentCreateRequest);
    }

    //Only registered students can place request -- Secure this API, only Students allowed to place request
    //BOOK_PLACE_REQUEST_AUTHORITY
    @PostMapping("/student/request")
    public String placeRequest(@RequestBody PlaceRequest placeRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        int id = user.getStudent().getId();
        return studentService.placeRequest(placeRequest,id);
    }


    //Only registered students can see their own account only -- Get their details from Security Context
    //STUDENT_ONLY_AUTHORITY
    @GetMapping("/student/")
    public Student getStudent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        int id = user.getStudent().getId();
        return studentService.getStudentById(id);
    }

    //Only admins can see all student's info by their id
    //STUDENT_INFO_AUTHORITY
    @GetMapping("/studentById/{id}")
    public Student getStudent(@Valid @PathVariable("id") Integer id){
        return studentService.getStudentById(id);
    }


}
