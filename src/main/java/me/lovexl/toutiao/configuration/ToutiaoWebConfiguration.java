package me.lovexl.toutiao.configuration;

import me.lovexl.toutiao.interceptor.LoginRequiredInterceptor;
import me.lovexl.toutiao.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //首先执行passportInterceptor的preHandle，然后执行loginRequiredInterceptor的preHandle，如果没有
        //Interceptor就执行Controller,接着执行postHandler
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting");
        super.addInterceptors(registry);
    }
}
