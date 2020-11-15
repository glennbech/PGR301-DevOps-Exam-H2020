package no.devops.exam.db

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "monster_data")
class Monster(

        @get:Id
        @get:NotBlank
        var monsterId: String? = null

       /* @JsonBackReference
        @OneToMany(mappedBy = "monster", fetch = FetchType.EAGER)
        var rarity: List<MonsterRarity> = emptyList()*/
)