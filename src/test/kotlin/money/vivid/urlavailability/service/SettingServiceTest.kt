package money.vivid.urlavailability.service

import money.vivid.urlavailability.db.entity.Setting
import money.vivid.urlavailability.db.repository.SettingRepository
import money.vivid.urlavailability.support.constant.SettingCodes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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
    fun availableCodes() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.AVAILABLE_CODES))
            .thenReturn((Setting(SettingCodes.AVAILABLE_CODES, "200,302,403", 102)))

        val availableCodes = settingService.availableCodes()

        assertEquals(3, availableCodes.size)
        assertTrue(availableCodes.contains(200))
        assertTrue(availableCodes.contains(302))
        assertTrue(availableCodes.contains(403))
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.AVAILABLE_CODES)
    }

    @Test
    fun updatePeriod() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.PERIOD))
            .thenReturn((Setting(SettingCodes.PERIOD, "5", 103)))

        assertDoesNotThrow { settingService.updatePeriod(10) }
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.PERIOD)
        Mockito.verify(settingRepository, Mockito.times(1)).save(Setting(SettingCodes.PERIOD, "10", 103))
    }

    @Test
    fun updateCodes() {
        Mockito
            .`when`(settingRepository.findByCode(SettingCodes.AVAILABLE_CODES))
            .thenReturn((Setting(SettingCodes.AVAILABLE_CODES, "200", 104)))

        assertDoesNotThrow { settingService.updateCodes(listOf(200, 302)) }
        Mockito.verify(settingRepository, Mockito.times(1)).findByCode(SettingCodes.AVAILABLE_CODES)
        Mockito.verify(settingRepository, Mockito.times(1)).save(Setting(SettingCodes.AVAILABLE_CODES, "200,302", 104))
    }

}