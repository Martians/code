package com.data.datasource.m3_dynamic;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile("multi-3")
@Slf4j
/**
 * 表明这是一个切面
 */
@Aspect
@Order(-10)
@Component
public class DataSourceAspect {

    /**
     * annotation aspect
     */
    @Before(value = "@annotation(targetSource)", argNames = "targetSource")
    public void before_annotation(JoinPoint point, TargetSource targetDataSource) throws Throwable {
        log.debug("befor aspect: value {}, signature {}", point.getSignature(), targetDataSource.value());
        DataSourceContextHolder.set(targetDataSource.value());

            //        if (!DataSourceContextHolder.contain(source)) {
            //            log.warn("aspect: value {}, signature {} not contain such source!", point.getSignature(), source);
            //        } else {
            //        }
    }

    @After(value = "@annotation(targetSource)", argNames = "targetSource")
    public void after_annotation(JoinPoint point, TargetSource targetDataSource) {
        DataSourceContextHolder.clear();
        log.debug("after aspect: value {}, signature {}", point.getSignature(), DataSourceContextHolder.get());
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
     * package aspect
     */
    //@Pointcut("execution(* com.data.control.y.*.*(..))")
    @Pointcut("execution(* com.data.datasource.m3_dynamic.UserController.get2_package())")
    public void aspect() {}

    final private String source = "db1";

    @Before("aspect()")
    public void before_package(JoinPoint point) throws Throwable {
        log.debug("befor package aspect: value {}, signature {}", point.getSignature(), source);
        DataSourceContextHolder.set(source);
    }

    @After("aspect()")
    public void after_package(JoinPoint point) {
        DataSourceContextHolder.clear();
        log.debug("after package aspect: value {}, signature {}", point.getSignature(), DataSourceContextHolder.get());
    }
}