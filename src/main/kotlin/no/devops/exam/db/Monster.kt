package no.devops.exam.db

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.*

@Entity
data class Monster(

        @JsonManagedReference
        @OneToMany(mappedBy = "monster", fetch = FetchType.EAGER)
        var rarity: List<MonsterRarity> = emptyList(),

        @Id
        @GeneratedValue
        var monsterId: UUID? = null
)