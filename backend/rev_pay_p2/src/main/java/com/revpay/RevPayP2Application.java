package com.revpay;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.revpay.entity.Role;
import com.revpay.repository.RoleRepository;

@SpringBootApplication
@EnableJpaRepositories("com.revpay.repository")
@EntityScan("com.revpay.entity")
public class RevPayP2Application {

	public static void main(String[] args) {
		SpringApplication.run(RevPayP2Application.class, args);
	}

	@Bean
	CommandLineRunner seedRoles(RoleRepository roleRepository) {
		return args -> {
			ensureRoleExists(roleRepository, "ROLE_PERSONAL");
			ensureRoleExists(roleRepository, "ROLE_BUSINESS");
		};
	}

	private void ensureRoleExists(RoleRepository roleRepository, String roleName) {
		roleRepository.findByName(roleName).orElseGet(() -> {
			Role role = new Role();
			role.setName(roleName);
			return roleRepository.save(role);
		});
	}

}
