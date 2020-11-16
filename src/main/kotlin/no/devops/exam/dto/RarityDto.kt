package no.devops.exam.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull

class RarityDto(

        @ApiModelProperty("The start value of the rarity calculation")
        @get:NotNull
        var startValue: Float,

        @ApiModelProperty("The end value of the rarity calculation")
        @get:NotNull
        var endValue: Float,

        @ApiModelProperty("The start and end value calculated into rarity")
        @get:NotNull
        var rarity: Int
)

