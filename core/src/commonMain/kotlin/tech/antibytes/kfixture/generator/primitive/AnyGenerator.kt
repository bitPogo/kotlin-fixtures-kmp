/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.PublicApi

internal object AnyGenerator : PublicApi.Generator<Any> {
    override fun generate() = Any()
    override fun generate(predicate: (Any) -> Boolean): Any {
        throw IllegalStateException("Any cannot be filtered!")
    }
}
