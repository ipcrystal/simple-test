package org.example.simpletest.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhuzhenjie
 * @since 2021/3/5
 */
@RequestMapping("/testValidate")
@RestController
@Slf4j
public class TestValidateController {

    /**
     * not null 验证测试
     *
     * @param validation
     * @return
     */
    @PostMapping("/notNull")
    public String validateNotNull(@Validated(NotNullV.class) @RequestBody Validation validation) {
        log.info(validation.toString());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " @NotNull 不能为null，但是可以为空字符串(ps：StringUtils.Empty)";
    }


    /**
     * not blank 验证测试
     *
     * @param validation
     * @return
     */
    @PostMapping("/notBlank")
    String validateNotBlank(@Validated(NotBlankV.class) @RequestBody Validation validation) {
        log.info(validation.toString());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " @NotBlank 不能为null且不能为空字符串";
    }


    /**
     * 测试dto
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Data
    public static class Validation {

        @NotNull(groups = {NotNullV.class}, message = "@NotNull 不能为null但可以为空字符串(ps：StringUtils.Empty")
        private String notNullStr;

        @NotBlank(groups = {NotBlankV.class}, message = "@NotBlank 不能为null且不能为空字符串")
        private String notBlankStr;

    }

    public interface NotNullV {
    }

    public interface NotBlankV {
    }
}
