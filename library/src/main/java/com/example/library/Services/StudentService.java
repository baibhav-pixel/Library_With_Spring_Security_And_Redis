package com.example.library.Services;

import com.example.library.Models.*;
import com.example.library.Repositories.StudentCacheRepository;
import com.example.library.Repositories.StudentRepository;
import com.example.library.Requests.PlaceRequest;
import com.example.library.Requests.StudentCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class StudentService {

    @Value("${STUDENT_ONLY_AUTHORITY}")
    private String STUDENT_ONLY_AUTHORITY;

    @Value("${BOOK_PLACE_REQUEST_AUTHORITY}")
    private String BOOK_PLACE_REQUEST_AUTHORITY;

    private String delimiter=":";
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminService adminService;

    @Autowired
    RequestService requestService;
    @Autowired
    BookService bookService;
    @Autowired
    MyUserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StudentCacheRepository studentCacheRepository;

    public Student createStudent(StudentCreateRequest studentCreateRequest) {
        //Allow User to be created first and then Student
        User userFromControllerRequest = studentCreateRequest.toUser();
        attachAuthorities(userFromControllerRequest);
        encodePassword(userFromControllerRequest);

        //save the student in db first and then save in the cache so that data is
        // not lost if we tried saving in cache first
        User savedUser = userService.saveUser(userFromControllerRequest);
        Student student = studentCreateRequest.to(savedUser); //student joined to user here

        //this step is a must as student id is generated after it is saved to db
        student = studentRepository.save(student);

        try
        {
            studentCacheRepository.saveStudentInCache(student);
        }
        catch (Exception e)
        {
            return null; //just to tell in UI that student is not created successfully
        }
        return student;
    }

    public String placeRequest(PlaceRequest placeRequest, int studentId) throws Exception {

        List<Admin> adminList = adminService.getAdmins();
        Book book = bookService.getBookById(placeRequest.getBookId());

        Admin admin = adminList.stream()
                .min(Comparator.comparingInt(a -> a.getRequestsToProcess().size()))
                .orElse(null);

        //If someone returns an already returned book and admin approves then throw error
        if(placeRequest.toRequest(studentId).getRequestType() == RequestType.RETURN && book.getStudent()==null
        && placeRequest.toRequest(studentId).getRequestStatus()== RequestStatus.PENDING)
        {
            placeRequest.toRequest(studentId).setRequestStatus(RequestStatus.REJECTED);
            throw new Exception("Can't return a book which is not issued");
        }
        return requestService.saveOrUpdateRequest(placeRequest.toRequest(admin,studentId)).getRequestId();
    }

    public void attachAuthorities(User user){
        String authorities = STUDENT_ONLY_AUTHORITY + delimiter + BOOK_PLACE_REQUEST_AUTHORITY;
        user.setAuthorities(authorities);
    }

    public void encodePassword(User user){
        String rawPwd = user.getPassword();
        String encodedPwd = passwordEncoder.encode(rawPwd);
        user.setPassword(encodedPwd);
    }

    public Student getStudentById(Integer id) {
        /*
            1. Search in cache, if found return from here
            2. Get from db, save it back to cache, return the student
         */
        Student student = studentCacheRepository.getStudentFromCache(id);

        if(student == null)// not in cache
        {
            student = studentRepository.findById(id).orElse(null); // check in DB

            if(student != null) // Available in DB
            {
                try
                {
                    studentCacheRepository.saveStudentInCache(student); // then save it back to cache
                }
                catch (Exception e) // if not in DB
                {
                    return null;
                }
            }

        }

        return student;
    }
}
