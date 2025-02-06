
plugins {
  id("java-library")
  kotlin("jvm")
//  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.apollo)
  id("maven-publish")
}

group = "com.fragments"
version = "1.0.0"

sourceSets() {
  create("stage") {
    java.srcDir("src/stage/java")
    java.srcDir("build/generated/source/apollo/stage")
  }

  create("prod") {
    java.srcDir("src/prod/java")
    java.srcDir("build/generated/source/apollo/prod")
  }
}

java {
  registerFeature("stage") {
    usingSourceSet(sourceSets["stage"])
    capability("com.fragments", "stage", "c")
  }

  registerFeature("prod") {
    usingSourceSet(sourceSets["prod"])
    capability("com.fragments", "prod", "c")
  }
}

dependencies {
  add("api", project(":schema"))
}

abstract class Wrapper @Inject constructor(val softwareComponentFactory: SoftwareComponentFactory)

val softwareComponent = objects.newInstance(Wrapper::class.java).softwareComponentFactory.adhoc("apollo")

apollo {

  service("stage") {
    packageName.set("com.generated")
    dependsOn(project(":schema"))

    srcDir("src/stage/graphql")

    outputDirConnection {
      connectToJavaSourceSet("stage")
    }

    outgoingVariantsConnection {
      addToSoftwareComponent(softwareComponent)
    }
  }

  service("prod") {
    packageName.set("com.generated")
    dependsOn(project(":schema"))

    srcDir("src/prod/graphql")

    outputDirConnection {
      connectToJavaSourceSet("prod")
    }

    outgoingVariantsConnection {
      addToSoftwareComponent(softwareComponent)
    }
  }

}

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("default") {
      artifact("${layout.buildDirectory.get().asFile}/intermediates/aar_main_jar/release/classes.jar")
    }
    create<MavenPublication>("apollo") {
      artifactId = "fragments-apollo"
      from(softwareComponent)
    }
  }
  repositories {
    maven {
      name = "localMaven"
      url = uri("file://${rootDir}/build/localMaven")
    }
  }
}