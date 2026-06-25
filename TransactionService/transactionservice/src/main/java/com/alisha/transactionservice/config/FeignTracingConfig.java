package com.alisha.transactionservice.config;

import feign.RequestInterceptor;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignTracingConfig {

    private final Tracer tracer;

    @Bean
    public RequestInterceptor tracingInterceptor() {

        return template -> {

            if (tracer.currentSpan() != null) {

                String traceId = tracer.currentSpan()
                        .context()
                        .traceId();

                String spanId = tracer.currentSpan()
                        .context()
                        .spanId();

                template.header(
                        "X-B3-TraceId",
                        traceId);

                template.header(
                        "X-B3-SpanId",
                        spanId);

                template.header(
                        "traceparent",
                        "00-" + traceId + "-" + spanId + "-01");
            }
        };
    }
}