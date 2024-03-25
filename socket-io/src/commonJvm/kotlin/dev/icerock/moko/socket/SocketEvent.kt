/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.socket

import io.socket.client.Manager.EVENT_RECONNECT
import io.socket.client.Manager.EVENT_RECONNECT_ATTEMPT
import io.socket.client.Manager.EVENT_RECONNECT_ERROR
import io.socket.client.Socket
import io.socket.client.Socket.EVENT_CONNECT_ERROR
import io.socket.client.Socket.EVENT_DISCONNECT
import io.socket.engineio.client.Socket.EVENT_ERROR
import io.socket.engineio.client.Socket.EVENT_MESSAGE
import io.socket.engineio.client.Socket.EVENT_PING
import io.socket.engineio.client.Socket.EVENT_PONG

actual sealed class SocketEvent<T> : Mapper<T> {
    actual object Connect : SocketEvent<Unit>(), Mapper<Unit> by UnitMapper() {
        override val socketIoEvents: List<String> = listOf(Socket.EVENT_CONNECT)
    }

    actual object Connecting : SocketEvent<Unit>(), Mapper<Unit> by UnitMapper() {
        override val socketIoEvents: List<String> = listOf("connecting")
    }

    actual object Disconnect : SocketEvent<Unit>(), Mapper<Unit> by UnitMapper() {
        override val socketIoEvents: List<String> = listOf(EVENT_DISCONNECT)
    }

    actual object Error : SocketEvent<Throwable>() {
        override val socketIoEvents: List<String> = listOf(
            EVENT_ERROR,
            EVENT_CONNECT_ERROR,
            EVENT_RECONNECT_ERROR
        )

        override fun mapper(array: Array<out Any>): Throwable {
            return array[0] as Throwable
        }
    }

    actual object Message : SocketEvent<Any>() {
        override val socketIoEvents: List<String> = listOf(EVENT_MESSAGE)
        override fun mapper(array: Array<out Any>): Any {
            return array
        }
    }

    actual object Reconnect : SocketEvent<Unit>(), Mapper<Unit> by UnitMapper() {
        override val socketIoEvents: List<String> = listOf(EVENT_RECONNECT)
    }

    actual object ReconnectAttempt : SocketEvent<Int>() {
        override val socketIoEvents: List<String> = listOf(EVENT_RECONNECT_ATTEMPT)
        override fun mapper(array: Array<out Any>): Int {
            return array[0] as Int
        }
    }

    actual object Ping : SocketEvent<Unit>(), Mapper<Unit> by UnitMapper() {
        override val socketIoEvents: List<String> = listOf(EVENT_PING)
    }

    actual object Pong : SocketEvent<Unit>(), Mapper<Unit> by UnitMapper() {
        override val socketIoEvents: List<String> = listOf(EVENT_PONG)
    }

    abstract val socketIoEvents: List<String>

    private class UnitMapper : Mapper<Unit> {
        override fun mapper(array: Array<out Any>) = Unit
    }
}
