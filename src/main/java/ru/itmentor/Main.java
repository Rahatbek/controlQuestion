package ru.itmentor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.itmentor.components.UsersProxy;
import ru.itmentor.config.ProjectConfig;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        UsersProxy usersProxy = context.getBean(UsersProxy.class);
        usersProxy.start();
        System.out.println(usersProxy.getKey());
    }
}