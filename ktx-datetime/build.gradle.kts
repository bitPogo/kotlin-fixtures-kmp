/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCounter
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoMeasurement
import tech.antibytes.gradle.publishing.api.DocumentationConfiguration
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.isIdea
import tech.antibytes.gradle.dependency.helper.nodePeerPackage
import tech.antibytes.gradle.dependency.helper.nodeProductionPackage
import tech.antibytes.gradle.kfixture.config.publishing.FixtureKtxDateTimeConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.gradle.antibytes.dokkaConfiguration)
}

group = FixtureKtxDateTimeConfiguration.group

antibytesPublishing {
    packaging.set(FixtureKtxDateTimeConfiguration.publishing.packageConfiguration)
    repositories.set(FixtureKtxDateTimeConfiguration.publishing.repositories)
    versioning.set(FixtureKtxDateTimeConfiguration.publishing.versioning)
    documentation.set(
        DocumentationConfiguration(
            tasks = setOf("dokkaHtml"),
            outputDir = buildDir.resolve("dokka")
        )
    )
    signing.set(FixtureKtxDateTimeConfiguration.publishing.signing)
}

antibytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.90)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.97)
    )

    val excludes = setOf(
        "**/DateTimeFixtureKt*",
        "**/InstantFixtureKt*",
    ) // Inline Function cannot be covered

    val jvmCoverage = JvmJacocoConfiguration.createJvmKmpConfiguration(
        project,
        classFilter = excludes,
        verificationRules = setOf(
            branchCoverage,
            instructionCoverage
        ),
    )

    val androidCoverage = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
        project,
        classFilter = excludes, // Inline Function cannot be covered
        verificationRules = setOf(
            branchCoverage,
            instructionCoverage
        ),
    )

    configurations.put("jvm", jvmCoverage)
    configurations.put("android", androidCoverage)
}

android {
    namespace = "tech.antibytes.kfixture.ktx.datetime"

    defaultConfig {
        minSdk = local.versions.minSdk.get().toInt()
    }
}

kotlin {
    android()

    js(IR) {
        nodejs()
        browser()
    }

    jvm()

    ios()
    iosSimulatorArm64()
    ensureAppleDeviceCompatibility()

    linuxX64()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalUnsignedTypes")
                optIn("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
                implementation(antibytesCatalog.common.kotlinx.dateTime)
                implementation(project(":core"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.common.test.kotlin.annotations)
            }
        }

        val noJsMain by creating {
            dependsOn(commonMain)
        }
        val noJsTest by creating {
            dependsOn(commonTest)
        }

        val androidMain by getting {
            dependsOn(noJsMain)
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
            }
        }
        if (!isIdea()) {
            val androidAndroidTestRelease by getting {
                dependsOn(noJsTest)
            }
            val androidAndroidTest by getting {
                dependsOn(noJsTest)
                dependsOn(androidAndroidTestRelease)
            }
            val androidTestFixturesDebug by getting {
                dependsOn(noJsTest)
            }
            val androidTestFixturesRelease by getting {
                dependsOn(noJsTest)
            }

            val androidTestFixtures by getting {
                dependsOn(noJsTest)
                dependsOn(androidTestFixturesDebug)
                dependsOn(androidTestFixturesRelease)
            }

            val androidTest by getting {
                dependsOn(androidTestFixtures)
            }
        }
        val androidTest by getting {
            dependsOn(noJsTest)

            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)

                nodePeerPackage(local.node.jsJodaTimezone)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)

                nodeProductionPackage(local.node.jsJodaTimezone)
            }
        }

        val jvmMain by getting {
            dependsOn(noJsMain)
        }
        val jvmTest by getting {
            dependsOn(noJsTest)
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
            }
        }

        val nativeMain by creating {
            dependsOn(noJsMain)
        }

        val nativeTest by creating {
            dependsOn(noJsTest)
        }

        val darwinMain by creating {
            dependsOn(nativeMain)
        }

        val darwinTest by creating {
            dependsOn(nativeTest)
        }

        val otherMain by creating {
            dependsOn(nativeMain)
        }

        val otherTest by creating {
            dependsOn(nativeTest)
        }

        val linuxX64Main by getting {
            dependsOn(otherMain)
        }

        val linuxX64Test by getting {
            dependsOn(otherTest)
        }

        val iosMain by getting {
            dependsOn(darwinMain)
        }

        val iosTest by getting {
            dependsOn(darwinTest)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}
