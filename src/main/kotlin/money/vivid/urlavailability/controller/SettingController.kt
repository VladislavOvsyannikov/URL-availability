package money.vivid.urlavailability.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import money.vivid.urlavailability.service.SettingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Settings")
@RequestMapping("/settings")
class SettingController(val settingService: SettingService) {

    @GetMapping
    @Operation(summary = "Return all application settings")
    fun settings() = settingService.settings()

    @PutMapping("/period")
    @Operation(summary = "Change period in minutes for URLs availability check (default is 1)")
    fun updatePeriod(
        @Parameter(description = "Period to change") @RequestParam period: Int
    ) = settingService.updatePeriod(period)

    @PutMapping("/availability-codes")
    @Operation(summary = "Change response HTTP codes which represents URL availability (default is [200])")
    fun updateCodes(
        @Parameter(description = "Codes to change") @RequestParam codes: List<Int>
    ) = settingService.updateCodes(codes)

}
