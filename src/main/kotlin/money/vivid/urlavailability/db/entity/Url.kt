package money.vivid.urlavailability.db.entity

import org.springframework.http.HttpMethod
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Url(

    @Column(nullable = false)
    var value: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var method: HttpMethod,

    @Column(nullable = false)
    var active: Boolean = true,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

)
