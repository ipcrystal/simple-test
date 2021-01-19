package org.example.simpletest.kotlin

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author zhuzhenjie
 * @since 2021/1/18
 */

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

fun getLogger(name: String): Logger = LoggerFactory.getLogger(name)