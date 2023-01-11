package com.example.library.Requests;

import com.example.library.Models.Admin;
import com.example.library.Models.User;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCreateRequest {
    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;

    public Admin to(User savedUser)
    {
        return Admin.builder()
                .name(name)
                .email(email)
                .user(savedUser)
                .build();
    }

    public Admin to(){
        return to(null);
    }

    public User toUser()
    {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
