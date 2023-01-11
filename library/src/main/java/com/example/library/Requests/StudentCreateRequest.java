package com.example.library.Requests;

import com.example.library.Models.Student;
import com.example.library.Models.User;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCreateRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String rollNo;
    @NotNull
    private String name;
    @Min(16)
    private Integer age;

    public Student to(User savedUser)
    {
        return Student.builder()
                .rollNo(rollNo)
                .name(name)
                .age(age)
                .user(savedUser)
                .build();
    }

    public Student to(){
        return to(null);
    }

    public User toUser() {
        return User.builder()
                .username(this.username)
                .password(this.password) //we need to encode this
                .build();
    }
}
