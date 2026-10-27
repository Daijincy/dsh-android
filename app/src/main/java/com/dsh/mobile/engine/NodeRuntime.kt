package com.dsh.mobile.engine

import android.content.Context

/**
 * v1 scaffold for the on-device Node runtime.
 *
 * The final implementation bundles a Node-for-Android binary plus the
 * @deepseek-ai/dsh engine bundle, launches `dsh host` in the app sandbox and
 * wires stdio JSON-RPC to the bridge. That shipping step is staged in P1; for
 * now this class reports `isReady = false` unless a runtime is found.
 */
class NodeRuntime(context: Context) {

    val isReady: Boolean = locateRuntime()

    private fun locateRuntime(): Boolean {
        // P1: look for a Node binary in filesDir/dsh/node or a bundled nodejs-mobile AAR.
        return false
    }

    fun start() {
        // P1: ProcessBuilder(nodeBinary, "node", "dsh", "host", "--profile", "sdk-app")
        //     then bridge stdio to the WebView via JSON-RPC.
    }

    fun stop() {
        // P1: kill the child process group.
    }
}
