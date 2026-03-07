package com.itis.oris;

import com.itis.oris.config.Config;
import com.itis.oris.model.Phone;
import com.itis.oris.repositories.PhoneRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        Phone phone = new  Phone();
        phone.setNumber("1234567890");
        phone.setId(1L);

        PhoneRepository phoneRepository = (PhoneRepository) context.getBean("phoneRepository");

        phoneRepository.save(phone);
    }
}