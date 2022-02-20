package money.vivid.urlavailability.db.repository

import money.vivid.urlavailability.db.entity.Url
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository : JpaRepository<Url, Long> {
    fun findByActiveIsTrue(pageable: Pageable): Page<Url>
    fun findByIdAndActiveIsTrue(id: Long): Url?
}