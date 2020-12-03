package sheep.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import sheep.portal.entity.WholePortal;


@SpringBootApplication
public class SheepPortalApplication {
	public static void main(String[] args) {
		SpringApplication.run(SheepPortalApplication.class, args);
	}

}
