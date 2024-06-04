package cn.linshio.webFlux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.reactive.config.*;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.reactive.socket.server.WebSocketService;

/**
 * @author linshio
 * @create 2024/6/4 14:10
 */
@Configuration
public class MyWebConfiguration {

    //配置底层
    @Bean
    public WebFluxConfigurer webFluxConfigurer(){
        return new WebFluxConfigurer() {
            @Override
            public void configureContentTypeResolver(RequestedContentTypeResolverBuilder builder) {
                WebFluxConfigurer.super.configureContentTypeResolver(builder);
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                WebFluxConfigurer.super.addCorsMappings(registry);
            }

            @Override
            public void configurePathMatching(PathMatchConfigurer configurer) {
                WebFluxConfigurer.super.configurePathMatching(configurer);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                WebFluxConfigurer.super.addResourceHandlers(registry);
            }

            @Override
            public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
                WebFluxConfigurer.super.configureArgumentResolvers(configurer);
            }

            @Override
            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
                WebFluxConfigurer.super.configureHttpMessageCodecs(configurer);
            }

            @Override
            public void addFormatters(FormatterRegistry registry) {
                WebFluxConfigurer.super.addFormatters(registry);
            }

            @Override
            public Validator getValidator() {
                return WebFluxConfigurer.super.getValidator();
            }

            @Override
            public MessageCodesResolver getMessageCodesResolver() {
                return WebFluxConfigurer.super.getMessageCodesResolver();
            }

            @Override
            public WebSocketService getWebSocketService() {
                return WebFluxConfigurer.super.getWebSocketService();
            }

            @Override
            public void configureViewResolvers(ViewResolverRegistry registry) {
                WebFluxConfigurer.super.configureViewResolvers(registry);
            }
        };
    }
}
