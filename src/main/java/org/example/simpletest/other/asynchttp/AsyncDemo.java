package org.example.simpletest.other.asynchttp;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.Objects;

import static org.asynchttpclient.Dsl.*;

/**
 * AsyncDemo
 *
 * @author zhuzhenjie
 * @since 2021/5/19
 */
public class AsyncDemo {

    private static final AsyncHttpClient client;

    static {
        client = asyncHttpClient();
    }


    /**
     * Close.
     */
    public static void close() {
        if (Objects.nonNull(client)) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        getDemo();

        postDemo();

        close();

    }

    static void getDemo() {
        try {
            Request request = get("http://prod.eblssmart.com:8090/realtime-data/listPlantInfo")
                    .build();
            ListenableFuture<Response> getResFuture = client.executeRequest(request);

            Response response = getResFuture.get();

            String responseBody = response.getResponseBody();

            System.out.println(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postDemo() {
        try {
            Request request = post("http://prod.eblssmart.com:8090/realtime-data/latestRealData")
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                            JSON.toJSONString(
                                    Plant.builder().plantName("栟茶").build()
                            )
                    )
                    .build();

            ListenableFuture<Response> postRes = client.executeRequest(request);

            Response response = postRes.get();

            String responseBody = response.getResponseBody();

            System.out.println(responseBody);

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    @Data
    static class Plant {
        String plantName;
    }
}
