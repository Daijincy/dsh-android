package com.dsh.mobile.bridge

import org.json.JSONObject

/**
 * Minimal JSON-RPC 2.0 helpers used between the frontend and the engine host.
 * Kept dependency-free so it also serves as the protocol reference for P1's
 * stdio/WS bridge to the on-device DSH host.
 */
object JsonRpc {

    fun request(id: Int, method: String, params: Any?): String {
        val o = JSONObject()
        o.put("jsonrpc", "2.0")
        o.put("id", id)
        o.put("method", method)
        if (params != null) o.put("params", params)
        return o.toString()
    }

    fun success(id: Int, result: Any?): String {
        val o = JSONObject()
        o.put("jsonrpc", "2.0")
        o.put("id", id)
        if (result != null) o.put("result", result)
        return o.toString()
    }

    fun error(id: Int, code: Int, message: String): String {
        val o = JSONObject()
        o.put("jsonrpc", "2.0")
        o.put("id", id)
        o.put("error", JSONObject().put("code", code).put("message", message))
        return o.toString()
    }

    fun parseMethod(json: String): String? =
        try { JSONObject(json).optString("method", "").ifEmpty { null } } catch (_: Exception) { null }
}
