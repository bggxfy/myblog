package com.bgg.common.exception;

import com.bgg.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理类
 */

@Slf4j
@RestControllerAdvice   //全局异常捕获
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)     //返回http状态码 400
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("服务器异常");
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ExpiredCredentialsException.class)
    public Result handler(ExpiredCredentialsException e){
        log.error("token已失效，请重新登录！");
        return Result.fail(401,e.getMessage(),null);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)    //401
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e){
        log.error("权限不足");
        return Result.fail(401,e.getMessage(),null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)    //方法参数不合规范异常
    public Result handler(MethodArgumentNotValidException e){
        log.error("数据校验异常");

        BindingResult bindingResult = e.getBindingResult();
        //获取异常数组
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder stringBuilder = new StringBuilder("");
        //异常信息拼串
        allErrors.forEach((objectError) -> {
            stringBuilder.append(String.valueOf(objectError.getDefaultMessage()));
            stringBuilder.append(" ");
        });
        return Result.fail(String.valueOf(stringBuilder));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)    //断言异常
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常");
        return Result.fail(String.valueOf(e.getMessage()));
    }

}
