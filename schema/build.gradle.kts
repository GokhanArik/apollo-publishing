
plugins {
  id("java-library")
  alias(libs.plugins.apollo)
  id("maven-publish")
}

group = "com.schema"
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
        capability("com.schema", "stage", "c")
    }

    registerFeature("prod") {
        usingSourceSet(sourceSets["prod"])
        capability("com.schema", "prod", "c")
    }
}


dependencies {
    add("stageApi", libs.apollo.api)
    add("prodApi", libs.apollo.api)
}

abstract class Wrapper @Inject constructor(val softwareComponentFactory: SoftwareComponentFactory)

val softwareComponent = objects.newInstance(Wrapper::class.java).softwareComponentFactory.adhoc("apollo")

apollo {
  service("stage") {
    schemaFile.set(file("src/stage/graphql/schema.graphqls"))

    packageName.set("com.generated")
    generateApolloMetadata.set(true)
    alwaysGenerateTypesMatching.add(".*")

    outputDirConnection {
      connectToJavaSourceSet("stage")
    }

    outgoingVariantsConnection {
      afterEvaluate {
        addToSoftwareComponent("java")

      }
    }
  }
  service("prod") {
    schemaFile.set(file("src/prod/graphql/schema.graphqls"))

    packageName.set("com.generated")
    generateApolloMetadata.set(true)
    alwaysGenerateTypesMatching.add(".*")

    outputDirConnection {
        connectToJavaSourceSet("stage")
    }

    outgoingVariantsConnection {
      afterEvaluate {
          addToSoftwareComponent("java")
      }
    }
  }
}

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("default") {
        from(components["java"])
    }
  }
  repositories {
    maven {
      name = "localMaven"
      url = uri("file://${rootDir}/build/localMaven")
    }
  }
}