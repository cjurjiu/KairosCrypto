package com.catalinjurjiu.kairoscrypto.businesslayer.model

/**
 * Enum of network errors which might occur.
 */
enum class ErrorCode {
    /**
     * A communication error has occurred. Maybe the connection was dropped too soon, or maybe
     * a 500 - internal server error was returned.
     */
    REQUEST_FAILED_ERROR,
    /**
     * A network timeout has occurred. Slow connection maybe?
     */
    TIMEOUT_ERROR,
    /**
     * A generic error has occurred. This could potentially span over a large number of HTTP Error
     * codes. Generally, it could be anything that's not [REQUEST_FAILED_ERROR] & [TIMEOUT_ERROR].
     */
    GENERIC_ERROR,
}