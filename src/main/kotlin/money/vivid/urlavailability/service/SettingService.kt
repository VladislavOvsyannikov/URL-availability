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

    fun availabilityCodes() = setting(SettingCodes.AVAILABILITY_CODES).value.split(",").map { it.toInt() }

    fun updatePeriod(period: Int) {
        val periodSetting = setting(SettingCodes.PERIOD)
        periodSetting.value = period.toString()
        settingRepository.save(periodSetting)
    }

    fun updateCodes(codes: List<Int>) {
        val codesSetting = setting(SettingCodes.AVAILABILITY_CODES)

        val value = codes
            .map { HttpStatus.valueOf(it).value() }
            .joinToString(separator = ",")

        codesSetting.value = value
        settingRepository.save(codesSetting)
    }

    private fun setting(code: String) = settingRepository.findByCode(code)
        ?: throw EntityNotFoundException("Setting with code = $code was not found")

}