package com.wj.sampleproject.base

data class ListEntity<E>(
        var list: ArrayList<E>,
        var refresh: Boolean
)