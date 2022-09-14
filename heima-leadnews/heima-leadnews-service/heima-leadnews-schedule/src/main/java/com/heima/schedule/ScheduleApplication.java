package com.heima.schedule;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

import static com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults.Encoding;


@SpringBootApplication
@MapperScan("com.heima.schedule.mapper")
@EnableScheduling//开启调度任务
public class ScheduleApplication {

    public static void main(String[] args) {
//        UUID uuid = UUID.randomUUID();
//        String StringToSign="GET&/%2F";
//        percentEncode
//                Base64( HMAC-SHA1( AccessKey Secret, UTF-8-Encoding-Of(StringToSign)) )
        SpringApplication.run(ScheduleApplication.class,args);
    }
    /**
     * mybatis-plus乐观锁支持
     * @return
     */
    @Bean
    public MybatisPlusInterceptor optimisticLockerInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


}
