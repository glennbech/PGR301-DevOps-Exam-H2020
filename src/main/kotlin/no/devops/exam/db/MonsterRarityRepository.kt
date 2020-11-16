package no.devops.exam.db

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface MonsterRarityRepository : CrudRepository<MonsterRarity, String> {
    @Query("SELECT avg(rarity) FROM MonsterRarity")
    fun calcRarity(): Double
}

