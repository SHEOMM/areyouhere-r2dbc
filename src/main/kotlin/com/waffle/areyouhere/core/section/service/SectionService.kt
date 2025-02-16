package com.waffle.areyouhere.core.section.service

import com.waffle.areyouhere.core.section.model.Section
import com.waffle.areyouhere.core.section.repository.SectionRepository
import com.waffle.areyouhere.core.section.service.dto.SectionDto
import com.waffle.areyouhere.core.section.service.dto.SectionSaveDto
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository,
) {
    suspend fun getMostRecent(courseId: Long): SectionDto? {
        return sectionRepository.findFirstByCourseIdOrderByCreatedAtDesc(courseId)?.let {
            SectionDto(it)
        }
    }

    suspend fun create(sectionSaveDto: SectionSaveDto) {
        sectionRepository.save(Section(sectionSaveDto))
    }
}
