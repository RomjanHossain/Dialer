package com.capx.dialer.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Domain-layer DI.
 *
 * All use cases are provided via their `@Inject` constructors (their only
 * dependencies are repository interfaces bound in [DataModule] and the
 * [com.capx.dialer.core.domain.repository.TelecomBridge] bound in
 * [TelecomModule]). Explicit `@Provides` methods are intentionally omitted —
 * declaring them here in addition to the `@Inject` constructors would create
 * duplicate Dagger bindings and fail the build.
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule
