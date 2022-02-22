package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Url
import money.vivid.urlavailability.db.repository.UrlRepository
import money.vivid.urlavailability.dto.UrlRequestDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpMethod
import javax.persistence.EntityNotFoundException

@ExtendWith(MockitoExtension::class)
internal class UrlServiceTest {

    @Mock
    lateinit var urlRepository: UrlRepository
    @InjectMocks
    lateinit var urlService: UrlService

    @Test
    fun urls() {
        val pageable = PageRequest.of(0, 20)

        Mockito
            .`when`(urlRepository.findByActiveIsTrue(pageable))
            .thenReturn(PageImpl(listOf(Url("https://example.com/", HttpMethod.HEAD, true, 100))))

        val urls = urlService.urls(pageable)

        assertEquals(1, urls.content.size)
        assertEquals(100, urls.content[0].id)
        Mockito.verify(urlRepository, Mockito.times(1)).findByActiveIsTrue(pageable)
    }

    @Test
    fun create_illegalUrl_exception() {
        val request = UrlRequestDto("string")

        assertThrows<IllegalArgumentException> { urlService.create(request) }
    }

    @Test
    fun create_correctUrl_successfulCreation() {
        val request = UrlRequestDto("https://example.com/")

        Mockito
            .`when`(urlRepository.save(any()))
            .thenReturn(Url("https://example.com/", HttpMethod.HEAD, true, 101))

        assertDoesNotThrow { urlService.create(request) }
        Mockito.verify(urlRepository, Mockito.times(1)).save(Url("https://example.com/", HttpMethod.HEAD, true))
    }

    @Test
    fun disable_activeUrlWasNotFound_exception() {
        val urlId = 102L

        assertThrows<EntityNotFoundException> { urlService.disable(urlId) }
    }

    @Test
    fun disable_activeUrlWasFound_disableUrl() {
        val urlId = 103L

        Mockito
            .`when`(urlRepository.findByIdAndActiveIsTrue(urlId))
            .thenReturn(Url("https://example.com/", HttpMethod.HEAD, true, urlId))

        assertDoesNotThrow { urlService.disable(urlId) }
        Mockito.verify(urlRepository, Mockito.times(1)).save(Url("https://example.com/", HttpMethod.HEAD, false, urlId))
    }

}
