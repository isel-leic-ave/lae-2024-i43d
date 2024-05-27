plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "lae-2024-i43d"
include("lesson09-reflect")
include("lesson09-TPC")
include("lesson10-logger")
include("lesson11-naive-mapper")
include("lesson12-naive-mapper-annotations")
include("lesson15-naive-mapper-recursive")
include("lesson18-exercises")
include("lesson20-cojen-maker")
include("lesson21-naive-mapper-dynamic")
include("lesson24-benchmarking")
include("lesson27-sequences")
include("lesson29-sequences-exercises")
include("lesson32-deconstructing-yield")
include("lesson33-suspend-functions")
include("lesson34-lazy-iterator-builder-yield")
include("lesson35-gc")
include("lesson36-closeable")
