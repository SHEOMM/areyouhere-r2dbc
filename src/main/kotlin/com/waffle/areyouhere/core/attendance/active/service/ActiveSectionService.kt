package com.waffle.areyouhere.core.attendance.active.service

import com.waffle.areyouhere.core.attendance.active.repository.ActiveSectionRepository
import org.springframework.stereotype.Service

@Service
class ActiveSectionService(
    private val activeSectionRepository: ActiveSectionRepository,
) {
    suspend fun isActivated(sectionId: Long): Boolean {
        activeSectionRepository.findBySectionId(sectionId)?.let {
            return true
        }
        return false
    }
}
