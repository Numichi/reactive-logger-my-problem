package com.example.demo.config

import org.springframework.context.annotation.Configuration
import reactor.core.CoreSubscriber
import reactor.core.Scannable
import reactor.core.publisher.Hooks
import reactor.core.publisher.Operators
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Configuration
class MdcContextLifterConfiguration {
    companion object {
        private val MDC_CONTEXT_REACTOR_KEY = MdcContextLifterConfiguration::class.java.name
    }

    @PostConstruct
    private fun contextOperatorHook() {
        Hooks.onEachOperator(MDC_CONTEXT_REACTOR_KEY,
            Operators.lift { _: Scannable?,
                             coreSubscriber: CoreSubscriber<in Any> ->
                MdcContextLifter(coreSubscriber)
            }
        )
    }

    @PreDestroy
    private fun cleanupHook() {
        Hooks.resetOnEachOperator(MDC_CONTEXT_REACTOR_KEY)
    }
}
