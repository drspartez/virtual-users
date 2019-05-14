package com.atlassian.performance.tools.virtualusers.mock

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.UserMemory
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.jiraactions.api.scenario.addMultiple
import kotlin.streams.toList


class ScenarioMock : Scenario {

    private val actionsPerThread : MutableMap<String, List<String>> = mutableMapOf()

    override fun getActions(jira: WebJira, seededRandom: SeededRandom, meter: ActionMeter): List<Action> {
        val scenario: MutableList<Action> = mutableListOf()
        val action1 : Action = NamedAction("action1")
        val action2 : Action = NamedAction("action2")
        val action3 : Action = NamedAction("action3")
        val action4 : Action = NamedAction("action4")
        val action5 : Action = NamedAction("action5")
        val action6 : Action = NamedAction("action6")
        val actionProportions = mapOf(
            action1 to 1,
            action2 to 1,
            action3 to 1,
            action4 to 1,
            action5 to 1,
            action6 to 1
        )
        actionProportions.entries.forEach { scenario.addMultiple(element = it.key, repeats = it.value) }
        scenario.shuffle(seededRandom.random)
        actionsPerThread.put(Thread.currentThread().name, scenario.stream().map { it as NamedAction }.map { it.getName() }.toList())
        return scenario
    }


    override fun getSetupAction(jira: WebJira?, meter: ActionMeter?): Action {
        return NamedAction("setup action")
    }

    override fun getLogInAction(jira: WebJira?, meter: ActionMeter?, userMemory: UserMemory?): Action {
        return NamedAction("login action")
    }

    fun getActionsPerThreads() : Map<String, List<String>> {
        return this.actionsPerThread
    }

    class NamedAction(private val name: String) : Action {

        fun getName() : String{
            return this.name;
        }

        override fun run() {
            //System.out.println(this.name)
        }
    }

}
