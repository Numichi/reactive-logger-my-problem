package com.example.demo.config

import org.reactivestreams.Subscription
import org.slf4j.MDC
import reactor.core.CoreSubscriber
import reactor.util.context.Context
import java.util.stream.Collectors

class MdcContextLifter<T>(var coreSubscriber: CoreSubscriber<T>) : CoreSubscriber<T> {
    override fun onSubscribe(subscription: Subscription) {
        copyToMdc(coreSubscriber.currentContext())
        coreSubscriber.onSubscribe(subscription)
    }

    override fun onNext(obj: T) {
        copyToMdc(coreSubscriber.currentContext())
        coreSubscriber.onNext(obj)
    }

    override fun onError(t: Throwable) {
        copyToMdc(coreSubscriber.currentContext())
        coreSubscriber.onError(t)
    }

    override fun onComplete() {
        copyToMdc(coreSubscriber.currentContext())
        coreSubscriber.onComplete()
    }

    override fun currentContext(): Context {
        return coreSubscriber.currentContext()
    }

    private fun copyToMdc(context: Context) {
        if (!context.isEmpty) {
            MDC.setContextMap(context.stream().collect(
                Collectors.toMap(
                    { (key): Map.Entry<Any, Any> -> key.toString() },
                    { (_, value): Map.Entry<Any, Any> -> value.toString() }
                )
            ))
        } else {
            MDC.clear()
        }
    }
}