package money.vivid.urlavailability.dto

import org.springframework.http.HttpMethod
import javax.validation.constraints.NotEmpty

class UrlRequestDto(

    @field:NotEmpty
    val url: String,

    val httpMethod: HttpMethod = HttpMethod.HEAD

)
