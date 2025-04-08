package com.ssuamje.composetoolkit.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

typealias JobKey = String
typealias ExecutedMillis = Long

@Composable
fun rememberDebouncer(scope: CoroutineScope = rememberCoroutineScope()): Debouncer {
    return remember { Debouncer(scope) }
}

class Debouncer(private val scope: CoroutineScope) {

    private val jobs = mutableMapOf<JobKey, Job>()
    private val recentExecutions = mutableMapOf<JobKey, ExecutedMillis>()

    fun debounce(
        key: String,
        duration: Duration = 0.3.seconds,
        blockAfterDebounceDuration: Duration? = null,
        block: suspend () -> Unit
    ) {
        val current = System.currentTimeMillis()
        val lastExecution = recentExecutions[key] ?: 0
        blockAfterDebounceDuration?.let {
            if (current - lastExecution < it.inWholeMilliseconds) return
        }

        jobs[key]?.cancel()
        jobs[key] = scope.launch {
            delay(duration)
            block()
            recentExecutions[key] = System.currentTimeMillis()
        }
    }

    fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        recentExecutions.clear()
    }
}