package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono

class WebExchangeFilter : ExchangeFilterFunction {
    companion object {
        private val logger = LoggerFactory.getLogger(WebExchangeFilter::class.java)
    }

    override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        return Mono.just(request)
            .doOnNext { logger.info("req") }
            .flatMap { next.exchange(it!!) }
            .doOnNext { _ -> logger.info("res") }
    }
}