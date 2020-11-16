package no.devops.exam.db

import com.fasterxml.jackson.annotation.JsonBackReference
import java.util.*
import javax.persistence.*

@Entity
data class MonsterRarity(
        var rarity: Int? = null,
        var startValue: Float? = null,
        var endValue: Float? = null,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "monster_id", nullable = false)
        var monster: Monster? = null,

        @Id
        var id: String? = null
)