/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import kotlin.random.nextULong
import tech.antibytes.kfixture.PublicApi

internal class ULongGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<ULong, ULong> {
    override fun generate(): ULong = random.nextULong()

    override fun generate(predicate: (ULong) -> Boolean): ULong {
        TODO("Not yet implemented")
    }

    override fun generate(from: ULong, to: ULong, predicate: (ULong) -> Boolean): ULong = random.nextULong(ULongRange(from, to))
}
