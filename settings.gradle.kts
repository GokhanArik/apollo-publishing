pluginManagement {
  listOf(repositories, dependencyResolutionManagement.repositories).forEach {
    it.mavenCentral()
    it.maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    it.google()
    it.maven("file://${rootDir}/build/localMaven")
  }
}


include(":schema")
include(":fragments")
include(":consumer")
include(":app")
