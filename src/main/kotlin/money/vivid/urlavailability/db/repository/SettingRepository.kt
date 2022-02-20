package money.vivid.urlavailability.db.repository

import money.vivid.urlavailability.db.entity.Setting
import org.springframework.data.jpa.repository.JpaRepository

interface SettingRepository : JpaRepository<Setting, Long> {
    fun findByCode(code: String): Setting?
}