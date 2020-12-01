package sheep.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//程序主入口 本身时spring的组件
@SpringBootApplication

@MapperScan("sheep.user")
public class SheepUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SheepUserApplication.class, args);
	}

}
