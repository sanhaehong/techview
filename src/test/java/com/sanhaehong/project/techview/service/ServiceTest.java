package com.sanhaehong.project.techview.service;

import com.sanhaehong.project.techview.InitAndDest;
import com.sanhaehong.project.techview.repository.AnswerRepository;
import com.sanhaehong.project.techview.repository.QuestionRepository;
import com.sanhaehong.project.techview.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public abstract class ServiceTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

}