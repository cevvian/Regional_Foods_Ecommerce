package edu.ut.sales.sales_analyst.configs;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI(
            @Value("${open.api.title}") String title,
            @Value("${open.api.version}") String version,
            @Value("${open.api.description}") String description
    ) {
        return new OpenAPI().info(new Info()
                .title(title)
                .version(version)
                .description(description)
                .contact(new Contact().name("Hieu Nhi Support").email("nlhieunhi1402@gmail.com"))
        );
    }
}
