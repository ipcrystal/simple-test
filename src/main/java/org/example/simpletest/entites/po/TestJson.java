package org.example.simpletest.entites.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
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

    @TableField(value = "json_obj_arr", typeHandler = JsonArrTypeHandler.class)
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

    public static class JsonArrTypeHandler extends AbstractJsonTypeHandler<List<TestJson>> {
        @Override
        protected List<TestJson> parse(String json) {
            return JSON.parseArray(json, TestJson.class);
        }

        @Override
        protected String toJson(List<TestJson> list) {
            return JSON.toJSONString(list, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
        }
    }
}
