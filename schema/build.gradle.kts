
plugins {
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.apollo)
  alias(libs.plugins.android.library)
  id("maven-publish")
}

group = "com.schema"
version = "1.0.0"

dependencies {
  add("api", libs.apollo.api)
}

abstract class Wrapper @Inject constructor(val softwareComponentFactory: SoftwareComponentFactory)

val softwareComponent = objects.newInstance(Wrapper::class.java).softwareComponentFactory.adhoc("apollo")

apollo {
  service("service1") {
    packageName.set("com.service1")
    generateApolloMetadata.set(true)
    alwaysGenerateTypesMatching.add(".*")
    outgoingVariantsConnection {
      afterEvaluate {
//        addToSoftwareComponent("release")
        addToSoftwareComponent(softwareComponent)

      }
    }
  }
  service("service2") {
    packageName.set("com.service2")
    generateApolloMetadata.set(true)
    alwaysGenerateTypesMatching.add(".*")
    outgoingVariantsConnection {
      afterEvaluate {
//        addToSoftwareComponent("release")
        addToSoftwareComponent(softwareComponent)

      }
    }
  }
}

android {
  namespace = "com.schema"
  compileSdk = libs.versions.android.sdkversion.compile.get().toInt()
  publishing {
    singleVariant("release")
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("default") {
//      afterEvaluate {
//        from(components["release"])
//      }
      artifact("${layout.buildDirectory.get().asFile}/intermediates/aar_main_jar/release/classes.jar")
    }
  }
  repositories {
    maven {
      name = "localMaven"
      url = uri("file://${rootDir}/build/localMaven")
    }
  }
}
