 package com.kingbo401.commons.validation;

import java.util.Set;
import java.util.StringJoiner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kingbo401.commons.enums.ErrorCode;
import com.kingbo401.commons.exception.MixedBagException;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.commons.validation.annotation.ParamCheck;

 /**
 * 方法参数校验aop
 * @author kingbo401
 * @date 2019年7月20日
 */
@Aspect
@Component
@Order(-999)
public class ParamCheckAop {
    /**
     * 校验工厂
     */
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    
    /**
     * 校验工具类
     */
    private static Validator validator = validatorFactory.getValidator();
    
    /**
     * 检测添加了ParamCheck注解的方法参数合法性
     * @param joinPoint
     * @param pc
     * @return
     * @throws Throwable
     */
    @Around("@annotation(pc)")
    public Object check(ProceedingJoinPoint joinPoint, ParamCheck pc) throws Throwable{
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return joinPoint.proceed();
        }
        for (Object arg : args) {
            Set<ConstraintViolation<Object>> violations = validator.validate(arg, pc.value());
            if (CollectionUtil.isEmpty(violations)) {
                continue;
            }
            StringJoiner joiner = new StringJoiner(", ");
            for(ConstraintViolation<Object> violation : violations){
                joiner.add(violation.getMessage());
            }
            throw new MixedBagException(ErrorCode.PARAM_INVALID, joiner.toString());
        }
        return joinPoint.proceed();
    }
}
