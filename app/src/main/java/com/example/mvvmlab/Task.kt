package com.example.mvvmlab

data class Task(
    val id: Int,
    val title: String,
    val done: Boolean = false
)