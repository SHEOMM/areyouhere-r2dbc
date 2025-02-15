package com.waffle.areyouhere.core.section.repository

import com.waffle.areyouhere.core.section.model.Section
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : CoroutineCrudRepository<Section, Long>
