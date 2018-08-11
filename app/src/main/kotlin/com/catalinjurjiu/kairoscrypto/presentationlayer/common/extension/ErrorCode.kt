package com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension

import android.support.annotation.StringRes
import com.catalinjurjiu.kairoscrypto.R
import com.catalinjurjiu.kairoscrypto.businesslayer.model.ErrorCode

@StringRes
fun ErrorCode.toMessageResId(): Int = when (this) {
    ErrorCode.GENERIC_ERROR -> R.string.error_generic_failure
    ErrorCode.TIMEOUT_ERROR -> R.string.error_request_timeout
    ErrorCode.REQUEST_FAILED_ERROR -> R.string.error_no_internet
}