package com.example.demo.controller

import com.example.demo.WebExchangeFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class MdcController {

    private val logger = LoggerFactory.getLogger(MdcController::class.java)

    @GetMapping("delay/{delay}")
    suspend fun _delay(@PathVariable delay: Int) {
        delay(delay.toLong())
    }

    /**
     * Example call:
     * http://localhost:8080/notwork/100?data=data
     *
     * Expected: should be visible context, In the case of both logins.
     *
     * @see com.example.demo.ExamplePlugin
     * Log:
     * {"message":"before - reactor-http-nio-1","context":{"data":"200","reactor.onDiscard.local":"reactor.core.publisher.Operators$$Lambda$797/0x0000000800640040@5b697809"}}
     * {"message":"req","context":{"data":"200","reactor.onDiscard.local":"reactor.core.publisher.Operators$$Lambda$797/0x0000000800640040@5b697809"}}
     * {"message":"res","context":{"data":"200","reactor.onDiscard.local":"reactor.core.publisher.Operators$$Lambda$797/0x0000000800640040@5b697809"}}
     * {"message":"after - reactor-http-nio-1","context":{}}
     */
    @GetMapping("notwork/{delay}")
    suspend fun notwork(@PathVariable delay: Int) {
        logger.info("before - ${Thread.currentThread().name}")

        WebClient.builder().filter(WebExchangeFilter()).build()
            .get().uri("http://localhost:8080/delay/$delay")
            .retrieve()
            .toBodilessEntity()
            .awaitSingleOrNull()

        logger.info("after - ${Thread.currentThread().name}")
    }
}