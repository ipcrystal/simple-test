package org.example.simpletest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.simpletest.entites.po.TestJson;
import org.springframework.stereotype.Repository;

/**
 * TestJsonMapper
 *
 * @author zhuzhenjie
 * @since 2020/12/7
 */
@Repository
@Mapper
public interface TestJsonMapper extends BaseMapper<TestJson> {
}
