package com.waffle.areyouhere.core.section.service

import com.waffle.areyouhere.core.section.model.Section
import com.waffle.areyouhere.core.section.repository.SectionRepository
import com.waffle.areyouhere.core.section.service.dto.SectionDto
import com.waffle.areyouhere.core.section.service.dto.SectionSaveDto
import com.waffle.areyouhere.core.section.service.dto.SectionWithAttendanceDto
import com.waffle.areyouhere.crossConcern.error.SectionNotFoundException
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository,
) {
    suspend fun create(sectionSaveDto: SectionSaveDto) {
        sectionRepository.save(Section(sectionSaveDto))
    }

    suspend fun get(sectionId: Long): SectionDto {
        return sectionRepository.findById(sectionId)?.let { SectionDto(it) }
            ?: throw SectionNotFoundException
    }

    suspend fun getByIds(sectionIds: List<Long>): List<SectionDto> {
        return sectionRepository.findByIdIn(sectionIds).map { SectionDto(it) }
    }

    suspend fun getMostRecent(courseId: Long): SectionDto? {
        return sectionRepository.findFirstByCourseIdOrderByCreatedAtDesc(courseId)?.let {
            SectionDto(it)
        }
    }

    suspend fun getAllWithAttendances(courseId: Long): List<SectionWithAttendanceDto> {
        return sectionRepository.findAllWithAttendanceByCourseId(courseId)
            .map { SectionWithAttendanceDto(it) }
    }

    suspend fun deleteAll(sectionIds: List<Long>) {
        sectionRepository.deleteByIdIn(sectionIds)
    }

    suspend fun delete(sectionId: Long) {
        sectionRepository.deleteById(sectionId)
    }
}
