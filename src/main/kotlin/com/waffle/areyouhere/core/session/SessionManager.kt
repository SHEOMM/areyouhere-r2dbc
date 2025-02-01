package com.waffle.areyouhere.core.session

import com.waffle.areyouhere.crossConcern.error.UnAuthorizeException
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import org.springframework.web.server.WebSession

@Component
class SessionManager {

    private val login = Login()

    suspend fun set(session: WebSession, sessionInfo: SessionInfo) {
        session.attributes[SESSION_KEY] = sessionInfo
        session.save().awaitSingleOrNull()
    }

    suspend fun remove(session: WebSession) {
        session.invalidate().awaitSingleOrNull()
    }

    suspend fun login(): Login {
        return login
    }

    inner class Login {
        suspend fun getManagerIdOrThrow(session: WebSession): Long {
            return getManagerIdOrNull(session) ?: throw UnAuthorizeException
        }

        suspend fun getManagerIdOrNull(session: WebSession): Long? {
            return session.getAttribute<SessionInfo>(SESSION_KEY)?.managerId
        }
    }

    companion object {
        const val SESSION_KEY = "user"
    }
}
