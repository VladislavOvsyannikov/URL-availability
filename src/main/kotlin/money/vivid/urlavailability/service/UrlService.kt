package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Url
import money.vivid.urlavailability.db.repository.UrlRepository
import money.vivid.urlavailability.dto.UrlDto
import money.vivid.urlavailability.dto.UrlRequestDto
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import javax.persistence.EntityNotFoundException

@Service
class UrlService(val urlRepository: UrlRepository) {

    fun urls(pageable: Pageable) = findByActiveIsTrue(pageable)
        .map { UrlDto(it.id, it.value, it.method) }

    fun findByActiveIsTrue(pageable: Pageable) = urlRepository.findByActiveIsTrue(pageable)

    fun create(request: UrlRequestDto) {
        validate(request.url)
        urlRepository.save(Url(request.url, request.httpMethod))
    }

    fun disable(id: Long) {
        val url = urlRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Url with id = $id was not found") }

        url.active = false
        urlRepository.save(url)
    }

    private fun validate(url: String) {
        UriComponentsBuilder.fromHttpUrl(url)
    }

}