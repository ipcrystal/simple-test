package org.example.simpletest.entites.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * TestJson
 *
 * @author zhuzhenjie
 * @since 2020/12/7
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
@TableName(value = "test_json", autoResultMap = true)
public class TestJson {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "str_arr", typeHandler = FastjsonTypeHandler.class)
    private List<String> stringList;

    @TableField(value = "json_obj", typeHandler = FastjsonTypeHandler.class)
    private TestJsonObj jsonObj;

    @TableField(value = "json_obj_arr", typeHandler = FastjsonTypeHandler.class)
    private List<TestJsonObj> jsonObjList;

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    @Data
    public static class TestJsonObj {
        private String name;
        private Integer age;
    }

}
