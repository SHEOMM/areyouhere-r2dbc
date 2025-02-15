package com.waffle.areyouhere.core.section.service

import com.waffle.areyouhere.core.section.repository.SectionRepository
import org.springframework.stereotype.Service

@Service
class SectionService(
    private val sectionRepository: SectionRepository,
)
