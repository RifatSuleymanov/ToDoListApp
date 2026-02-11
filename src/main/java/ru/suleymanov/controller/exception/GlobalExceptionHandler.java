package ru.suleymanov.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

@Controller
public class GlobalExceptionHandler implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @Autowired
    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        int statusCode = response.getStatus();
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return switch (status) {
            case NOT_FOUND -> "public/error/not-found-error-page";
            case FORBIDDEN -> "public/error/forbidden-error-page";
            default -> "public/error/common-error-page";
        };
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException(NoHandlerFoundException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "public/error/not-found-error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "public/error/common-error-page";
    }

    @RequestMapping("/error/403")
    public String getForbiddenErrorPage() {
        return "public/error/forbidden-error-page";
    }

    @RequestMapping("/error/500")
    public String getCommonErrorPage() {
        return "public/error/common-error-page";
    }

    @RequestMapping("/error/404")
    public String getNotFoundErrorPage() {
        return "public/error/not-found-error-page";
    }
}
