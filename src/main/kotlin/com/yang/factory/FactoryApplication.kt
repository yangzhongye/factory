package com.yang.factory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FactoryApplication

fun main(args: Array<String>) {
    runApplication<FactoryApplication>(*args)
}
