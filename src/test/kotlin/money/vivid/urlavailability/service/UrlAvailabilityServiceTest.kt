package money.vivid.urlavailability.service

import money.vivid.urlavailability.config.properties.AppProperties
import money.vivid.urlavailability.db.entity.Url
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient

@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UrlAvailabilityServiceTest {

    @Mock
    lateinit var urlService: UrlService
    @Mock
    lateinit var settingService: SettingService
    @Mock
    lateinit var urlStatsService: UrlStatsService
    @Spy
    var appProperties: AppProperties = AppProperties(100, 5)
    @Spy
    var webClient: WebClient = WebClient.builder().build()
    @InjectMocks
    lateinit var urlAvailabilityService: UrlAvailabilityService

    var mockWebServer = MockWebServer()

    @BeforeAll
    fun beforeAll() {
        mockWebServer.start()

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/test" -> MockResponse().setResponseCode(200)
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }

    @Test
    fun checkAvailability() {
        val port = mockWebServer.port

        Mockito.`when`(settingService.availabilityCodes()).thenReturn(listOf(200))
        Mockito.`when`(urlService.findByActiveIsTrue(any()))
            .thenReturn(PageImpl(listOf(
                        Url("http://localhost:$port/test", HttpMethod.GET),
                        Url("http://localhost:$port/not-found", HttpMethod.GET),
                        Url("http://localhost:${port + 1}/test", HttpMethod.GET)
                    )))
            .thenReturn(Page.empty())

        urlAvailabilityService.checkAvailability()
        Thread.sleep(1000)

        Mockito.verify(urlStatsService, times(1))
            .create(true, Url("http://localhost:$port/test", HttpMethod.GET))
        Mockito.verify(urlStatsService, times(1))
            .create(false, Url("http://localhost:$port/not-found", HttpMethod.GET))
        Mockito.verify(urlStatsService, times(1))
            .create(false, Url("http://localhost:${port + 1}/test", HttpMethod.GET))
    }

    @AfterAll
    fun afterAll() {
        mockWebServer.shutdown()
    }

}
