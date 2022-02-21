package money.vivid.urlavailability.service

import money.vivid.urlavailability.config.properties.AppProperties
import money.vivid.urlavailability.db.entity.Url
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.util.Optional

@Service
class UrlAvailabilityService(
    val urlService: UrlService,
    val settingService: SettingService,
    val urlStatsService: UrlStatsService,
    val webClient: WebClient,
    val appProperties: AppProperties
) {

    private val log = KotlinLogging.logger {}

    fun checkAvailability() {
        log.info { "Availability check starts" }

        val availabilityCodes = settingService.availabilityCodes()
        val pageable = PageRequest.of(0, appProperties.pageSize, Sort.by("id"))
        var page = urlService.findByActiveIsTrue(pageable)

        while (page.hasContent()) {
            page.content
                .forEach { checkUrlAvailability(it, availabilityCodes) }

            page = urlService.findByActiveIsTrue(page.pageable.next())
        }
    }

    private fun checkUrlAvailability(url: Url, availabilityCodes: List<Int>) {
        val mono = webClient
            .method(url.method)
            .uri(url.value)
            .retrieve()
            .toBodilessEntity()

        mono
            .map { Optional.of(it.statusCodeValue) }
            .onErrorResume(WebClientResponseException::class.java) { Mono.just(Optional.of(it.rawStatusCode)) }
            .onErrorResume(WebClientException::class.java) { Mono.just(Optional.empty()) }
            .map { availabilityCodes.contains(it.orElse(null)) }
            .subscribe { urlStatsService.create(it, url) }
    }

}
