package org.example.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SpringUtils {

    private ApplicationContext applicationContext;

    /**
     * 重新注册bean
     *
     * @param beanName 需要重新初始化的bean名称
     */
    public void refreshBean(String beanName) {
        log.info("refresh bean name:{}", beanName);
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.destroySingleton(beanName);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanName);
        defaultListableBeanFactory.registerSingleton(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
