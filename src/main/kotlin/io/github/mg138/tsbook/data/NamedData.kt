package io.github.mg138.tsbook.data

interface NamedData : Data {
    fun getDataId(): Identifier
}