package org.example.simpletest.web;

import org.example.simpletest.entites.po.TestJson;
import org.example.simpletest.service.TestJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhuzhenjie
 * @since 2020/12/7
 */
@RequestMapping("/testJson")
@RestController
public class TestJsonController {

    @Autowired
    private TestJsonService testJsonService;

    @GetMapping("/listAll")
    public List<TestJson> listAllTestJsons() {
        return testJsonService.listAllTestJsons();
    }

    @GetMapping("/getById/{id}")
    public TestJson getById(@PathVariable(name = "id", required = true) Integer id) {
        return testJsonService.getById(id);
    }

    @GetMapping("/addRandomly")
    public Integer randomAdd() {
        return testJsonService.addRandomly();
    }
}
