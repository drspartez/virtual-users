package com.atlassian.performance.tools.virtualusers.mock

import com.atlassian.performance.tools.jiraactions.api.memories.User
import com.atlassian.performance.tools.virtualusers.UserGenerator
import com.atlassian.performance.tools.virtualusers.api.VirtualUserOptions
import java.util.concurrent.atomic.AtomicInteger

class VirtualUserGeneratorMock : UserGenerator {

    val usersCreated = AtomicInteger(0)

    override fun generateUser(options: VirtualUserOptions): User {
        return User("admin-${usersCreated.incrementAndGet()}", "admin")
    }

}
