package com.shizq.bika.core.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val INSTANCE_KEY =
    "com.arkivanov.decompose.lifecycle.ComponentContextCoroutineScope.INSTANCE_KEY"

val ComponentContext.componentScope: CoroutineScope
    get() {
        val scope = instanceKeeper.get(INSTANCE_KEY)
        if (scope is CoroutineScope) return scope

        if (lifecycle.state == Lifecycle.State.DESTROYED) {
            try {
                scope?.onDestroy()
            } catch (e: Exception) {
                throw RuntimeException(e)
            } finally {
                instanceKeeper.remove(INSTANCE_KEY)
            }
        }

        return DestroyableCoroutineScope(Dispatchers.Main.immediate + SupervisorJob()).also {
            instanceKeeper.put(INSTANCE_KEY, it)
        }
    }

class DestroyableCoroutineScope(
    context: CoroutineContext,
) : CoroutineScope,
    InstanceKeeper.Instance {

    override val coroutineContext: CoroutineContext = context

    override fun onDestroy() {
        coroutineContext.cancel()
    }
}
