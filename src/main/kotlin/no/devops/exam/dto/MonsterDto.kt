package no.devops.exam.dto

import io.swagger.annotations.ApiModelProperty

class MonsterDto(

        @get:ApiModelProperty("The id of the monster")
        var monsterId: String? = null

)