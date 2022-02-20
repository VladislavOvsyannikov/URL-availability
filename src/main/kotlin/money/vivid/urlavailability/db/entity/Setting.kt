package money.vivid.urlavailability.db.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Setting(

    @Column(nullable = false)
    var code: String,

    @Column(nullable = false)
    var value: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

)
