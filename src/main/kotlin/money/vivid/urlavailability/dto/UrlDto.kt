package money.vivid.urlavailability.dto

import org.springframework.http.HttpMethod

class UrlDto(val id: Long?, val url: String, val httpMethod: HttpMethod)
