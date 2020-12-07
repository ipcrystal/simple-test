package org.example.simpletest.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.example.simpletest.entites.po.TestJson;
import org.example.simpletest.mapper.TestJsonMapper;
import org.example.simpletest.service.TestJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author zhuzhenjie
 * @since 2020/12/7
 */
@Service
@Slf4j
public class TestJsonServiceImpl implements TestJsonService {

    @Autowired
    TestJsonMapper testJsonMapper;

    @Override
    public List<TestJson> listAllTestJsons() {
        return testJsonMapper.selectList(null);
    }

    @Override
    public TestJson getById(Integer id) {
        return testJsonMapper.selectById(id);
    }

    @Override
    public int addRandomly() {

        TestJson testJson = TestJson.builder()
                .stringList(IntStream.range(0, 5).mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toList()))
                .jsonObj(TestJson.TestJsonObj.builder().name(UUID.randomUUID().toString().substring(0, 8)).age(30).build())
                .jsonObjList(IntStream.range(0, 3)
                        .mapToObj(i -> TestJson.TestJsonObj.builder()
                                .name(UUID.randomUUID().toString().substring(0, 5))
                                .age(RandomUtils.nextInt(18, 30)).build())
                        .collect(Collectors.toList()))
                .build();

        log.info("add randomly | {}", testJson);

        return testJsonMapper.insert(testJson);
    }
}
