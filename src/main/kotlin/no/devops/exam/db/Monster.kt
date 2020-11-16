package no.devops.exam.db

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank

@Entity
data class Monster(

        /*@JsonManagedReference
        @OneToMany(mappedBy = "monster", fetch = FetchType.EAGER)
        var rarity: List<MonsterRarity> = emptyList(),*/

        @get:Id
        @get:NotBlank
        var monsterId: String? = null


)