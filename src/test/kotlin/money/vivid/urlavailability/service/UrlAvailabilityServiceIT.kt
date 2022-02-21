package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Url
import money.vivid.urlavailability.db.repository.UrlRepository
import money.vivid.urlavailability.db.repository.UrlStatsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UrlAvailabilityServiceIT {

    @Autowired
    lateinit var urlAvailabilityService: UrlAvailabilityService
    @Autowired
    lateinit var urlRepository: UrlRepository
    @Autowired
    lateinit var urlStatsRepository: UrlStatsRepository
    @Autowired
    lateinit var urlStatsService: UrlStatsService
    @LocalServerPort
    var port = 0

    @Test
    fun checkAvailability() {
        initUrls()
        val start = LocalDateTime.now().minusMinutes(1)
        val end = LocalDateTime.now().plusMinutes(1)

        urlAvailabilityService.checkAvailability()
        Thread.sleep(750) // wait for responses

        assertEquals(4, urlStatsRepository.findAll().size)
        assertTrue(urlStatsService.available(1, start, end))
        assertFalse(urlStatsService.available(2, start, end))
        assertFalse(urlStatsService.available(3, start, end))
        assertFalse(urlStatsService.available(4, start, end))
        assertThrows<IllegalArgumentException> { (urlStatsService.available(5, start, end)) }
    }

    private fun initUrls() {
        urlRepository.save(Url("http://localhost:$port/tests/test/", HttpMethod.HEAD))
        urlRepository.save(Url("http://localhost:$port/tests/test/", HttpMethod.GET))
        urlRepository.save(Url("http://localhost:$port/tests/sleep/", HttpMethod.GET))
        urlRepository.save(Url("http://localhost:${port + 1}/tests/test/", HttpMethod.HEAD))
        urlRepository.save(Url("https://exapmle.com/", HttpMethod.GET, false))
    }

}
