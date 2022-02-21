package money.vivid.urlavailability.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import money.vivid.urlavailability.config.properties.AppProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig(val appProperties: AppProperties) {

    @Bean
    fun webClient(): WebClient {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, appProperties.timeout)
            .doOnConnected { it.addHandler(ReadTimeoutHandler(appProperties.timeout.toLong(), TimeUnit.MILLISECONDS)) }

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }

}
