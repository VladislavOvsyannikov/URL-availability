package money.vivid.urlavailability.db.repository

import money.vivid.urlavailability.db.entity.UrlStats
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UrlStatsRepository : JpaRepository<UrlStats, Long> {

    fun existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(
        urlId: Long,
        start: LocalDateTime,
        end: LocalDateTime
    ) : Boolean

    fun existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(
        urlId: Long,
        start: LocalDateTime,
        end: LocalDateTime
    ) : Boolean

}
