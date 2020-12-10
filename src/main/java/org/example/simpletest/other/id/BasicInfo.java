package org.example.simpletest.other.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhuzhenjie
 * @since 2020/12/10
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class BasicInfo {

    /**
     * 基础应用消息
     */
    private String basicAppName;
}
