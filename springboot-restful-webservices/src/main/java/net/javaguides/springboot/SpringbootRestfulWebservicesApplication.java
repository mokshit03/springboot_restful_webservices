package net.javaguides.springboot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.javaguides.springboot.service.PasswordUpdateService;

// @SpringBootApplication
@SpringBootApplication(scanBasePackages = "net.javaguides.springboot")
@EnableTransactionManagement
public class SpringbootRestfulWebservicesApplication implements CommandLineRunner {

	private final PasswordUpdateService passwordUpdateService;

    public SpringbootRestfulWebservicesApplication(PasswordUpdateService passwordUpdateService) {
        this.passwordUpdateService = passwordUpdateService;
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestfulWebservicesApplication.class, args);
	}
	@Override
    public void run(String... args) throws Exception {
        passwordUpdateService.updatePasswords();
    }
}