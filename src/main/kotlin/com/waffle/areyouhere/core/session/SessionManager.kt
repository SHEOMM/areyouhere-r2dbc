package com.waffle.areyouhere.core.session

import org.springframework.stereotype.Component
import org.springframework.web.server.WebSession

@Component
class SessionManager {

    suspend fun set(session: WebSession, sessionInfo: SessionInfo) {
        session.attributes[SESSION_KEY] = sessionInfo
    }

    companion object {
        const val SESSION_KEY = "user"
    }
}
