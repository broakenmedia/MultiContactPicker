package com.wafflecopter.contactspicker

import androidx.annotation.Keep

@Keep
enum class LimitColumn {
    EMAIL, PHONE, NONE
}

@Keep
enum class ChoiceMode {
    CHOICE_MODE_MULTIPLE, CHOICE_MODE_SINGLE
}

@Keep
enum class LoadMode {
    LOAD_ASYNC, LOAD_SYNC
}