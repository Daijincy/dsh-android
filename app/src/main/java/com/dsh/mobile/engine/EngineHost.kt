package com.dsh.mobile.engine

import android.content.Context

/**
 * Engine host owns the lifecycle of the agent backend.
 *
 * Three modes:
 *  - DEMO  : a scripted engine runs inside the frontend (JS) so the UI is fully
 *            verifiable without a live backend. Default in v1.
 *  - REMOTE: connect to a DSH host over a WS/stdio endpoint (SDK mode).
 *  - LOCAL : run @deepseek-ai/dsh on-device via a bundled Node runtime (P1).
 */
class EngineHost(private val context: Context) {

    enum class Mode { DEMO, REMOTE, LOCAL }

    private var mode: Mode = Mode.DEMO
    private var node: NodeRuntime? = null
    @Volatile private var remoteUrl: String? = null

    fun connectRemote(url: String): String {
        remoteUrl = url
        mode = Mode.REMOTE
        return statusJson()
    }

    /** Attempts to start the on-device Node runtime + DSH host. Falls back to DEMO. */
    fun startLocal(): String {
        val runtime = NodeRuntime(context)
        if (runtime.isReady) {
            runtime.start()
            node = runtime
            mode = Mode.LOCAL
        } else {
            node = null
            mode = Mode.DEMO
        }
        return statusJson()
    }

    fun stop() {
        node?.stop()
        node = null
    }

    fun statusJson(): String {
        return """{"mode":"${mode.name.lowercase()}","nodeReady":${node?.isReady ?: false},"remoteUrl":"${remoteUrl ?: ""}","engine":"dsh-android v0.1.0"}"""
    }
}
