package whu.english_learn.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@Configuration
@MapperScan("whu.english_learn.mapper")
public class MyBatisConfig {
    
    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(DifficultyLevelTypeHandler.class);
        };
    }
} 