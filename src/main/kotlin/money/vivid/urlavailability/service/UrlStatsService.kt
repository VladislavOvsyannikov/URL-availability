package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Url
import money.vivid.urlavailability.db.entity.UrlStats
import money.vivid.urlavailability.db.repository.UrlStatsRepository
import money.vivid.urlavailability.dto.AvailableDto
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UrlStatsService(val urlStatsRepository: UrlStatsRepository) {

    private val log = KotlinLogging.logger {}

    fun create(available: Boolean, url: Url) {
        val urlStats = UrlStats(available, url)
        urlStatsRepository.save(urlStats)

        log.info { "[UrlStats created] [url: ${url.value}] [method: ${url.method}] [available: $available]" }
    }

    fun available(urlId: Long, start: LocalDateTime, end: LocalDateTime): AvailableDto {
        val existsCheck = urlStatsRepository
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, start, end)

        if (!existsCheck) {
            throw IllegalArgumentException("No data for this URL on this time range")
        }

        val existsUnavailableCheck = urlStatsRepository
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(urlId, start, end)

        return AvailableDto(!existsUnavailableCheck)
    }

}
