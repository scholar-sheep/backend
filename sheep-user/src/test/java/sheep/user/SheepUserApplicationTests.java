package sheep.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//单元测试
@SpringBootTest
class SheepUserApplicationTests {

	@Autowired
	DataSource dataSource;
	@Test
	void contextLoads() throws SQLException {
		System.out.println(dataSource.getConnection());
		dataSource.getConnection().close();
	}

}
