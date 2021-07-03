package com.zsc.finance.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.zsc.finance.common.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 扩展SpringMVC的功能
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override //这里配置静态资源访问路径
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //springboot默认静态资源访问路径就是以下的路径 所以不必再设置 但是若配置有拦截器 则需要在拦截器里面放行静态资源
        //若静态资源路径不是在springboot规定的静态资源文件夹下面，则需要在下面添加映射路径
        //springboot规定的静态资源文件夹
        //1. "classpath:/META-INF/resources/",
        //2. "classpath:/resources/",
        //3. "classpath:/static/",
        //4. "classpath:/public/"
        //5. "/"：当前项目的根路径
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        //registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }

    //无业务逻辑跳转
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //项目运行后直接访问或访问index.html（不存在index.html页面）界面跳转到login登录界面
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/forgetPassword").setViewName("forgetPassword");
        registry.addViewController("/emailTemplate").setViewName("emailTemplate");
        //registry.addViewController("/resetPassword").setViewName("resetPassword");
    }

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*
            anon: 无需认证就可访问
            authc：必须认证才能访问
            user：必须拥有记住我功能才能访问
            perms: 拥有对某个资源的权限才能访问
            role:拥有某个角色权限才能访问
       */

        Map<String, String> filterMap = new LinkedHashMap<>();

        //授权
//        filterMap.put("/user/add","perms[user:add]");
//        filterMap.put("/user/update","perms[user:update]");
        //授权
//        filterMap.put("/user/**", "roles[user]");
//        filterMap.put("/admin/**", "roles[admin]");
//
//
////
//////        filterMap.put("/user/add","authc");
//////        filterMap.put("/user/update","authc");
//
        //对所有请求认证
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
//        filterMap.put("/**", "authc");
//
//
//        filterMap.put("/user/*", "authc");
//        filterMap.put("/admin/*", "authc");

        return bean;
    }

    //DafaultWebSecurituManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);

        return securityManager;
    }

    //创建realm对象 ，需要自定义
    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //整合thymeleaf
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }
}
