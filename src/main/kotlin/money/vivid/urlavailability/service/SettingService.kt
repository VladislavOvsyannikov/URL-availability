package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.repository.SettingRepository
import money.vivid.urlavailability.dto.SettingDto
import money.vivid.urlavailability.support.constant.SettingCodes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class SettingService(val settingRepository: SettingRepository) {

    fun settings() = settingRepository.findAll().map { SettingDto(it.id, it.code, it.value) }

    fun period() = setting(SettingCodes.PERIOD).value.toInt()

    fun availableCodes() = setting(SettingCodes.AVAILABLE_CODES).value.split(",").map { it.toInt() }

    fun updatePeriod(period: Int) {
        val periodSetting = setting(SettingCodes.PERIOD)
        periodSetting.value = period.toString()
        settingRepository.save(periodSetting)
    }

    fun updateCodes(statuses: List<Int>) {
        val statusesSetting = setting(SettingCodes.AVAILABLE_CODES)

        val value = statuses
            .map { HttpStatus.valueOf(it).value() }
            .joinToString(separator = ",")

        statusesSetting.value = value
        settingRepository.save(statusesSetting)
    }

    private fun setting(code: String) = settingRepository.findByCode(code)
        ?: throw EntityNotFoundException("Setting with code = $code was not found")

}