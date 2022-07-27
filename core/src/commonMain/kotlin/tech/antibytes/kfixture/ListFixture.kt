/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi.Qualifier

/**
 * Creates a MutableList of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the List.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.mutableListFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T> = ::fixture,
): MutableList<T> {
    val actualSize = determineCollectionSize(size)

    return MutableList(actualSize) {
        nestedGenerator.invoke(qualifier)
    }
}

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableListFixtureAlias")
/**
 * Creates a MutableList of values for a given type.
 * @param T the type which is supposed to be created.
 * @param C the enclosing List.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the List.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableList<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableList<*>>,
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T> = ::fixture,
): C = mutableListFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
) as C

/**
 * Creates a List of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the List.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.listFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T> = ::fixture,
): List<T> = mutableListFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("listFixtureAlias")
/**
 * Creates a List of values for a given type.
 * @param T the type which is supposed to be created.
 * @param C the enclosing List.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the List.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : List<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<List<*>>,
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T> = ::fixture,
): C = listFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
) as C
