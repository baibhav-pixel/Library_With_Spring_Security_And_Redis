package com.example.library.Configurations;

import com.example.library.Services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {
    @Autowired
    MyUserService myUserService;
    @Value("${ADMIN_ONLY_AUTHORITY}")
    private String ADMIN_ONLY_AUTHORITY;

    @Value("${BOOK_CREATION_AUTHORITY}")
    private String BOOK_CREATION_AUTHORITY;

    @Value("${STUDENT_ATTENDANCE_AUTHORITY}")
    private String STUDENT_ATTENDANCE_AUTHORITY;

    @Value("${STUDENT_INFO_AUTHORITY}")
    private String STUDENT_INFO_AUTHORITY;

    @Value("${STUDENT_ONLY_AUTHORITY}")
    private String STUDENT_ONLY_AUTHORITY;

    @Value("${BOOK_PLACE_REQUEST_AUTHORITY}")
    private String BOOK_PLACE_REQUEST_AUTHORITY;

    @Bean
    protected DaoAuthenticationConfigurer<AuthenticationManagerBuilder, MyUserService>
    configure(AuthenticationManagerBuilder auth) throws Exception{
        return auth.userDetailsService(myUserService);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/admin/**").hasAuthority(ADMIN_ONLY_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/book").hasAuthority(BOOK_CREATION_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/student/request").hasAuthority(BOOK_PLACE_REQUEST_AUTHORITY)
                .antMatchers(HttpMethod.GET,"/student/").hasAuthority(STUDENT_ONLY_AUTHORITY)
                .antMatchers(HttpMethod.GET,"/studentById/**").hasAuthority(STUDENT_INFO_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/student").permitAll()
                .and()
                .formLogin();
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
