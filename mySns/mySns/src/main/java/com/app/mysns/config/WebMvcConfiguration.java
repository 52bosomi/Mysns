package com.app.mysns.config;
import com.app.mysns.filter.GlobalFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    private static final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:/templates/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 캐시 설정이 필요할 수 있음
        registry.addResourceHandler("/static/fonts/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/fonts/");
        registry.addResourceHandler("/static/images/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/images/");
        registry.addResourceHandler("/static/img/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/img/");
        registry.addResourceHandler("/static/css/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/css/");
        registry.addResourceHandler("/static/js/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/js/");
        registry.addResourceHandler("/static/vendor/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/vendor/");
        registry.addResourceHandler("/static/favicon.ico").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/images/icons/phodo.ico");
        // registry.addResourceHandler("/*.html").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    // 필터는 모든 요청에 대해서 쿠키 검증!!!
    @Bean
    FilterRegistrationBean myFilterRegistration () {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new GlobalFilter());
        filter.setOrder(0);
        filter.addUrlPatterns("/*");
        return filter;
    }

}