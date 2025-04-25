package com.o0u0o.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * <h1>服务日志切面</h1>
 * @author o0u0o
 * @description 服务日志切面
 * @date 2025/4/23 08:53
 */
@Slf4j
@Component
@Aspect
public class ServiceLogAspect {


    /**
     * 注解：@Around 环绕切面
     * 切面表达式
     * 1、 * 表示任意返回类型，比如 void object list等
     * 2、com.o0u0o.service.impl指定包名，要去具体切入切面的位置（某个java class所在的包的位置）
     * 3、.. 可以匹配到当前包以及他的子包
     * 4、* 表示可以匹配到当前包和子包下的java class
     * 5、.无意义
     * 6、* 代表任意的方法
     * 7、(..)表示方法参数，..表示任意参数
     *
     * 入参讲解：
     * 1、ProceedingJoinPoint 切点
     * @return Object
     */
    @Around("execution(* com.o0u0o.service.impl..*.*(..))")
    public Object recodeTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();
//        String pointName = joinPoint.getTarget().getClass().getName()
//                +"."+ ;

        long endTime = System.currentTimeMillis();


        return proceed;
    }
}
