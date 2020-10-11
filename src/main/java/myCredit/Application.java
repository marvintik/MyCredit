package myCredit;

import lombok.SneakyThrows;
import myCredit.utils.ConvertUnicode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }
}

