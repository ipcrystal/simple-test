package org.example.simpletest.other.txt;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuzhenjie
 * @since 2020/12/10
 */
public class ProjectQidong {
    public static void main(String[] args) {
        try {
            List<String> lines = IOUtils.readLines(new ClassPathResource("qd_ny.txt").getInputStream(), Charset.defaultCharset());
            lines.stream().map(line -> line.split("__")).map(strs -> strs[1]).distinct().collect(Collectors.toList()).forEach(System.out::println);
//            lines.stream().filter(line -> !line.contains("__")).collect(Collectors.toList()).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
