package com.zorkdata.datasynchro;

import com.zorkdata.datasynchro.biz.AppProgramBiz;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.zorkdata.datasynchro.mapper")
@ComponentScan(basePackages = {"com.zorkdata.datasynchro"})
public class DataSynchroApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataSynchroApplication.class, args);
		// Synchronize data
	}
}
