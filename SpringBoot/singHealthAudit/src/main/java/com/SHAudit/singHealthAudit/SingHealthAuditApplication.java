package com.SHAudit.singHealthAudit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.SHAudit.singHealthAudit.mySQLAccount.AccountRepository")
@SpringBootApplication(scanBasePackages={"com.SHAudit.singHealthAudit.mySQLAccount",
		"com.SHAudit.singHealthAudit.jwt","com.SHAudit.singHealthAudit.Auditor","com.SHAudit.singHealthAudit" })
public class SingHealthAuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingHealthAuditApplication.class, args);
	}

}
