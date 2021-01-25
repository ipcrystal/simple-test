package org.example.simpletest.kotlin.other.rudong

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.example.simpletest.kotlin.getLogger
import java.util.*

/**
 * @author zhuzhenjie
 * @since 2021/1/18
 */
private val log = getLogger("MqttTest-Rudong")

private const val broker = "tcp://47.101.184.101:1883"
private val clientId = "rd-test-${UUID.randomUUID()}"

private val topics = arrayOf(
    "/topic/rudong/fl-test", "/topic/rudong/bc-test",
    "/topic/rudong/hk-test", "/topic/rudong/yk-test"
)

fun main() {
    val client: MqttClient
    try {
        client = MqttClient(broker, clientId, MemoryPersistence())

        client.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                log.error("connection lost", cause)
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                log.info("--------------------------------------------")
                log.info("TOPIC   —> $topic")
                log.info("message —> ${message?.let { String(it.payload) }}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                log.info("delivery complete ${token?.isComplete}")
            }
        })
        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.connectionTimeout = 10
        options.keepAliveInterval = 20
        client.connect(options)

        client.subscribe(topics)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        log.info("如东mqtt测试客户端${clientId}启动...")
    }
}
