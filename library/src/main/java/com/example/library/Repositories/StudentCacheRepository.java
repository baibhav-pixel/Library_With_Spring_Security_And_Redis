package com.example.library.Repositories;

import com.example.library.Models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class StudentCacheRepository {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    private static final String KEY_PREFIX = "std::";
    private static final Integer KEY_EXPIRY = 20;

    public void saveStudentInCache(Student student) throws Exception {
        if(student.getId() == 0)
            throw new Exception("Id not present for the student");
        redisTemplate.opsForValue().set(getKey(student.getId()), student, KEY_EXPIRY, TimeUnit.MINUTES);
    }

    public Student getStudentFromCache(Integer studentId){
        if(studentId == null){
            return null;
        }

        return (Student) redisTemplate.opsForValue().get(getKey(studentId));
    }

    private String getKey(int studentId){
        return KEY_PREFIX + studentId;
    }
}
