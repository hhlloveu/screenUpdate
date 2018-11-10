package com.wiwj.screen.update;

import com.wiwj.screen.update.mvc.UpdateController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UpdateApplication {

    public static void main(String[] args) {
        UpdateController.baseDir = System.getProperty("user.dir") + "/";
        SpringApplication.run(UpdateApplication.class, args);
    }
}
