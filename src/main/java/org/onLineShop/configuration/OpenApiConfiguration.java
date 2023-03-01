package org.onLineShop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@OpenAPIDefinition
public class OpenApiConfiguration {
	  @Bean
	    public OpenAPI openApi() {
	        return new OpenAPI()
	                .info(new Info()
	                        .title("ecommerce-shop documentation")
	                        .description("cette documentation permet de facilite la tache au devellopeur front-end")
	                        .version("v1.0.1")
	                        .contact(new Contact()
	                               .name("KAMGA")
	                                //.url("https://asbnotebook.com")
	                                .email("andokamga@gmail.com"))
	                        .termsOfService("TOC")
	                        .license(new License().name("License").url("#"))
	                );
	    }

}
