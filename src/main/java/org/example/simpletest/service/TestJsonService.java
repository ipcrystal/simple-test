package org.example.simpletest.service;

import org.example.simpletest.entites.po.TestJson;

import java.util.List;

/**
 * TestJsonService
 *
 * @author zhuzhenjie
 * @since 2020/12/7
 */
public interface TestJsonService {

    /**
     * 获取全表数据
     *
     * @return
     */
    List<TestJson> listAllTestJsons();

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    TestJson getById(Integer id);

    /**
     * 测试添加
     *
     * @return
     */
    int addRandomly();
}
