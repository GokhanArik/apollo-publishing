
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.apollo)
}

dependencies {
  add("implementation", libs.apollo.api)
  add("implementation", "com.fragments:fragments:1.0.0")
  add("implementation", "com.schema:schema:1.0.0")
}

apollo {
  service("service1") {
    packageName.set("com.service1")
    dependsOn("com.fragments:fragments-apollo:1.0.0")
  }
  service("service2") {
    packageName.set("com.service2")
    dependsOn("com.fragments:fragments-apollo:1.0.0")
  }
}


android {
  namespace = "com.schema"
  compileSdk = libs.versions.android.sdkversion.compile.get().toInt()
  kotlinOptions {
    jvmTarget = "1.8"
  }
}