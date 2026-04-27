package com.example.retrotrade.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

private const val NAV_ANIMATION_DURATION = 750


fun AnimatedContentTransitionScope<*>.slideInFromRight() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(NAV_ANIMATION_DURATION)
    ) + fadeIn(tween(NAV_ANIMATION_DURATION))

fun AnimatedContentTransitionScope<*>.slideOutToLeft() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(NAV_ANIMATION_DURATION)
    ) + fadeOut(tween(NAV_ANIMATION_DURATION))

fun AnimatedContentTransitionScope<*>.slideInFromLeft() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(NAV_ANIMATION_DURATION)
    ) + fadeIn(tween(NAV_ANIMATION_DURATION))

fun AnimatedContentTransitionScope<*>.slideOutToRight() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(NAV_ANIMATION_DURATION)
    ) + fadeOut(tween(NAV_ANIMATION_DURATION))