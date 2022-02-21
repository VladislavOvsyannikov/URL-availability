package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Setting
import money.vivid.urlavailability.db.repository.SettingRepository
import money.vivid.urlavailability.support.constant.SettingCodes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class SettingServiceTest {

    @Mock
    lateinit var settingRepository: SettingRepository
    @InjectMocks
    lateinit var settingService: SettingService

    @Test
    fun settings() {
        Mockito
            .`when`(settingRepository.findAll())
            .thenReturn(listOf(Setting("code", "value", 100)))

        val settings = settingService.settings()

        assertEquals(1, settings.size)
        assertEquals(100, settings[0].id)
        Mockito.verify(settingRepository, Mockito.times(1)).findAll()
    }

    @Test
    fun period() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.PERIOD))
            .thenReturn((Setting(SettingCodes.PERIOD, "5", 101)))

        val period = settingService.period()

        assertEquals(5, period)
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.PERIOD)
    }

    @Test
    fun availabilityCodes() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.AVAILABILITY_CODES))
            .thenReturn((Setting(SettingCodes.AVAILABILITY_CODES, "200,302,403", 102)))

        val availabilityCodes = settingService.availabilityCodes()

        assertEquals(3, availabilityCodes.size)
        assertTrue(availabilityCodes.contains(200))
        assertTrue(availabilityCodes.contains(302))
        assertTrue(availabilityCodes.contains(403))
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.AVAILABILITY_CODES)
    }

    @Test
    fun updatePeriod_periodLessThanOne_exception() {
        assertThrows<IllegalArgumentException> { settingService.updatePeriod(0) }
    }

    @Test
    fun updatePeriod_periodGreaterThanZero_successfulUpdate() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.PERIOD))
            .thenReturn((Setting(SettingCodes.PERIOD, "5", 103)))

        assertDoesNotThrow { settingService.updatePeriod(10) }
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.PERIOD)
        Mockito.verify(settingRepository, Mockito.times(1)).save(Setting(SettingCodes.PERIOD, "10", 103))
    }

    @Test
    fun updateCodes_incorrectHttpCode_exception() {
        assertThrows<IllegalArgumentException> { settingService.updateCodes(listOf(200, 123, 302)) }
    }

    @Test
    fun updateCodes_correctHttpCodes_successfulUpdate() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.AVAILABILITY_CODES))
            .thenReturn((Setting(SettingCodes.AVAILABILITY_CODES, "200", 104)))

        assertDoesNotThrow { settingService.updateCodes(listOf(200, 302)) }
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.AVAILABILITY_CODES)
        Mockito
            .verify(settingRepository, Mockito.times(1))
            .save(Setting(SettingCodes.AVAILABILITY_CODES, "200,302", 104))
    }

}
