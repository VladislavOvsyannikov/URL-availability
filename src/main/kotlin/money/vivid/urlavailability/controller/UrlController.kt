package money.vivid.urlavailability.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import money.vivid.urlavailability.dto.UrlRequestDto
import money.vivid.urlavailability.service.UrlService
import money.vivid.urlavailability.support.PageableApi
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "URLs")
@RequestMapping("/urls")
class UrlController(val urlService: UrlService) {

    @GetMapping
    @PageableApi
    @Operation(summary = "Return URL entities with pagination")
    fun urls(@Parameter(hidden = true) pageable: Pageable) = urlService.urls(pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add URL to availability check. httpMethod - method called by check (default is HEAD)")
    fun create(
        @RequestBody @Validated request: UrlRequestDto
    ) = urlService.create(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove URL from availability check")
    fun disable(
        @Parameter(description = "Id of URL entity") @PathVariable id: Long
    ) = urlService.disable(id)

}