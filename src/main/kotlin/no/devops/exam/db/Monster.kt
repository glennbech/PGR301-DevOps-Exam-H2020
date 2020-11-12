package no.devops.exam.db

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "monster_data")
class Monster(

        @get:Id
        @get:NotBlank
        var monsterId: String? = null
)