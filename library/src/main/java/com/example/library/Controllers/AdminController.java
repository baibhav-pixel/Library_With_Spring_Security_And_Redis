package com.example.library.Controllers;

import com.example.library.Models.Admin;
import com.example.library.Models.User;
import com.example.library.Requests.AdminCreateRequest;
import com.example.library.Requests.ProcessRequest;
import com.example.library.Response.ProcessResponse;
import com.example.library.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    //Only Admins can create other admins -- secured endpoint, first admin to be created manually to act as super-user
    //ADMIN_ONLY_AUTHORITY
    @PostMapping("/admin")
    public Admin createAdmin(@Valid @RequestBody AdminCreateRequest adminCreateRequest){
        return adminService.createAdmin(adminCreateRequest);
    }

    //Only admins can see the list of admins -- secured endpoint
    //ADMIN_ONLY_AUTHORITY
    @GetMapping("/admin")
    public List<Admin> getAdmins()
    {
        return adminService.getAdmins();
    }


    //Only admins can process request -- secured endpoint
    //ADMIN_ONLY_AUTHORITY
    @PostMapping("/admin/process")
    public ProcessResponse processRequest(@Valid @RequestBody ProcessRequest processRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        int id = user.getAdmin().getId();
        return adminService.processRequest(processRequest,id);
    }
}
