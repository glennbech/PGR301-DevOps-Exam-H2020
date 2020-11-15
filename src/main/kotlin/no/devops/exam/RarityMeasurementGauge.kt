package no.devops.exam

import io.micrometer.core.instrument.MeterRegistry
import no.devops.exam.db.MonsterRarityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class RarityMeasurementGauge(

) : ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private lateinit var monsterRarityRepository: MonsterRarityRepository

    @Autowired
    private lateinit var meterRegistry: MeterRegistry

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        meterRegistry.gauge("Monster Rarity Calculation", monsterRarityRepository) {
            val value = monsterRarityRepository.calcRarity()
            value
        }
    }


}