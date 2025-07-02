package site.backrer.backed.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.backrer.backed.utils.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "请求参数格式错误";
        if (ex.getCause() instanceof JsonParseException) {
            JsonParseException jpe = (JsonParseException) ex.getCause();
            errorMessage = jpe.getOriginalMessage();
        } else if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            errorMessage = String.format("字段 '%s' 的值 '%s' 类型不正确，期望类型: %s",
                    ife.getPath().get(ife.getPath().size()-1).getFieldName(),
                    ife.getValue(),
                    ife.getTargetType().getSimpleName());
        }
        return Result.error("400", errorMessage);
    }
}
