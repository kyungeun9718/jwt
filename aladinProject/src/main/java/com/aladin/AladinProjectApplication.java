package com.aladin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.db.SQLiteManager;

@SpringBootApplication()
@MapperScan(basePackages = {
	    "com.aladin.user.mapper",
	    "com.aladin.todo.mapper"
	})
public class AladinProjectApplication {

	public static void main(String[] args) {
	SQLiteManager manager = new SQLiteManager();
	
	manager.createConnection();
  //  manager.closeConnection();      // 연결 해제
  //  manager.ensureConnection();     // 재연결
	SpringApplication.run(AladinProjectApplication.class, args);
	}

}