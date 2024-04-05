package me.vaimon.summarizer.util

interface Mapper<T, E> {
    fun from(e: E): T

    fun to(t: T): E
}