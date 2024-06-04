package cn.linshio.webFlux.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author linshio
 * @create 2024/6/4 10:35
 */
//全局异常捕获
@RestControllerAdvice
public class GlobalExceptionHandler {

    //传统的异常捕获
    @ExceptionHandler(ArithmeticException.class)
    public String error(ArithmeticException arithmeticException){
        System.out.println("发生了数学运算异常->" + arithmeticException);
        return "后台炸了";
    }
}
