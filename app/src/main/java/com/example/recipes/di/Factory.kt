package com.example.recipes.di

interface Factory<T> {
    fun create(): T
}