package com.atlassian.performance.tools.virtualusers

import com.atlassian.performance.tools.virtualusers.api.TemporalRate
import com.atlassian.performance.tools.virtualusers.api.VirtualUserLoad
import com.atlassian.performance.tools.virtualusers.api.VirtualUserOptions
import com.atlassian.performance.tools.virtualusers.api.browsers.HeadlessChromeBrowser
import com.atlassian.performance.tools.virtualusers.api.config.VirtualUserBehavior
import com.atlassian.performance.tools.virtualusers.api.config.VirtualUserTarget
import com.atlassian.performance.tools.virtualusers.measure.ApplicationNode
import com.atlassian.performance.tools.virtualusers.measure.JiraNodeCounter
import com.atlassian.performance.tools.virtualusers.mock.ScenarioMock
import com.atlassian.performance.tools.virtualusers.mock.VirtualUserGeneratorMock
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.After
import org.junit.Test
import java.net.URI

import java.time.Duration
import java.util.*


class LoadTestStabilityTest {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    private val anticipatedLoad = VirtualUserLoad.Builder()
        .maxOverallLoad(TemporalRate(300.0, Duration.ofSeconds(20)))
        .virtualUsers(5)
        .flat(Duration.ofSeconds(5))
        .build()

    private val vuBehaviorInstance = VirtualUserBehavior.Builder(
        scenario = ScenarioMock::class.java
    ).load(anticipatedLoad)
        .seed(Random().nextLong())
        .diagnosticsLimit(64)
        .skipSetup(true)
        .browser(HeadlessChromeBrowser::class.java)
        .build()


    @Test
    fun stabilityTest() {
        val loadTestOptions = VirtualUserOptions(
            target = VirtualUserTarget(
                webApplication = URI("http://localhost"),
                userName = "admin",
                password = "admin"
            ),
            behavior = vuBehaviorInstance
        )

        val test = LoadTest(loadTestOptions, VirtualUserGeneratorMock(), TestNodeCounter());
        test.run()
        val lastScenario = test.getScenario() as ScenarioMock;
        lastScenario.getActionsPerThreads().forEach {
            logger.info("Thread ${it.key} => ${it.value.joinToString()}");
        }
    }


    internal class TestNodeCounter() : JiraNodeCounter() {

        override fun count(node: ApplicationNode) {
        }

        override fun dump(target: Appendable) {
        }
    }
}
