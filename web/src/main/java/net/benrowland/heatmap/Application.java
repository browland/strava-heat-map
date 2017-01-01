package net.benrowland.heatmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableSpringDataWebSupport
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
