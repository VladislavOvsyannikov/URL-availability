package money.vivid.urlavailability.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import money.vivid.urlavailability.service.UrlStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@Tag(name = "URL stats")
@RequestMapping("/url-stats")
class UrlStatsController(val urlStatsService: UrlStatsService) {

    @GetMapping("/available")
    @Operation(summary = "Whether the URL was available at every moment of the given time range")
    fun urls(
        @Parameter(description = "Id of URL entity") @RequestParam urlId: Long,
        @Parameter(description = "Start of range") @RequestParam start: LocalDateTime,
        @Parameter(description = "End of range") @RequestParam end: LocalDateTime
    ) = urlStatsService.available(urlId, start, end)

}