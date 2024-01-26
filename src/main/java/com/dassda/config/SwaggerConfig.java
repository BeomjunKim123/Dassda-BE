package com.dassda.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String REFERENCE = "Authorization";

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan("com.dassda",
                        "com.dassda.kakao",
                        "com.dassda.oauth",
                        "com.dassda.request",
                        "com.dassda.response",
                        "com.dassda.token")
                .addOpenApiCustomizer(openAPIDefinition())
                .build();
    }
    private OpenApiCustomizer openAPIDefinition() {
        return openApi -> {
            openApi.info(new Info().title("다쓰다")
                    .version("v1")
                    .description("공유 일기장을 시작해볼까?")
                    .contact(new Contact().name("권동휘").email("tnqlsdld1@naver.com")));
            openApi.addSecurityItem(new SecurityRequirement().addList(REFERENCE));
            openApi.components(new Components()
                    .addSecuritySchemes(REFERENCE, new SecurityScheme()
                            .name(REFERENCE)
                            .type(SecurityScheme.Type.HTTP)
                            .in(SecurityScheme.In.HEADER)
                            .scheme("Bearer")
                            .name("Authorization Bearer도 붙이셈")));
        };
    }
}
