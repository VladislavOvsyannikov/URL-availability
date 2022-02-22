package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Url
import money.vivid.urlavailability.db.repository.UrlStatsRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpMethod
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class UrlStatsServiceTest {

    @Mock
    lateinit var urlStatsRepository: UrlStatsRepository
    @InjectMocks
    lateinit var urlStatsService: UrlStatsService

    @Test
    fun create() {
        val url = Url("https://example.com/", HttpMethod.HEAD, true, 100)

        assertDoesNotThrow { urlStatsService.create(true, url) }
        Mockito.verify(urlStatsRepository, Mockito.times(1)).save(any())
    }

    @Test
    fun available_noChecks_exception() {
        val urlId = 101L
        val date = LocalDateTime.now()

        Mockito
            .`when`(urlStatsRepository.existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, date, date))
            .thenReturn(false)

        assertThrows<IllegalArgumentException> { urlStatsService.available(urlId, date, date) }
        Mockito
            .verify(urlStatsRepository, Mockito.times(1))
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, date, date)
        Mockito
            .verify(urlStatsRepository, Mockito.times(0))
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(urlId, date, date)
    }

    @Test
    fun available_existsUnavailableCheck_false() {
        val urlId = 102L
        val date = LocalDateTime.now()

        Mockito
            .`when`(urlStatsRepository.existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, date, date))
            .thenReturn(true)
        Mockito
            .`when`(urlStatsRepository
                .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(urlId, date, date))
            .thenReturn(true)

        assertFalse(urlStatsService.available(urlId, date, date).result)
        Mockito
            .verify(urlStatsRepository, Mockito.times(1))
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, date, date)
        Mockito
            .verify(urlStatsRepository, Mockito.times(1))
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(urlId, date, date)
    }

    @Test
    fun available_allChecksAreAvailable_true() {
        val urlId = 102L
        val date = LocalDateTime.now()

        Mockito
            .`when`(urlStatsRepository.existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, date, date))
            .thenReturn(true)
        Mockito
            .`when`(urlStatsRepository
                .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(urlId, date, date))
            .thenReturn(false)

        assertTrue(urlStatsService.available(urlId, date, date).result)
        Mockito
            .verify(urlStatsRepository, Mockito.times(1))
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThan(urlId, date, date)
        Mockito
            .verify(urlStatsRepository, Mockito.times(1))
            .existsByUrlIdAndCreatedAtGreaterThanAndCreatedAtLessThanAndAvailableIsFalse(urlId, date, date)
    }

}
