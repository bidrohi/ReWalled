package com.bidyut.tech.rewalled.model

enum class Filter(
    private val value: String,
) {
    Best("best"),
    Controversial("controversial"),
    Hot("hot"),
    New("new"),
    // Random("random"), // TODO: Redirects to a different endpoint with different response
    Rising("rising"),
    Top("top"),
    ;

    override fun toString(): String = value

    companion object {
        fun fromString(
            value: String
        ): Filter = entries.firstOrNull { it.value == value } ?: Rising
    }
}
