pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// ---------------------------------------------------------------------------------
// «همیار سلامتی من» — نسخهٔ سلامتی/آرامش/ذهن‌آگاهی روی همان موتور همیار من.
//
// این ریپو مستقل است: بستهٔ `com.hamyarehealth.ir`، آیکون و رابط کاربری اختصاصی،
// ولی همان محتوای باکت (ParsPack)، همان حساب Appwrite و همان کلید رمز HTML.
// ---------------------------------------------------------------------------------
rootProject.name = "HamyarHealth"

include(
    ":hamyar-health",
    ":core-common",
    ":core-designsystem",
    ":core-appwrite",
    ":core-security",
    ":core-notifications",
    ":core-sync",
    ":feature-pairing",
    ":feature-hearttoheart",
    ":feature-calls",
    ":feature-playback",
    ":feature-study",
)

project(":hamyar-health").projectDir = file("apps/hamyar-health")
project(":core-common").projectDir = file("shared/core-common")
project(":core-designsystem").projectDir = file("shared/core-designsystem")
project(":core-appwrite").projectDir = file("shared/core-appwrite")
project(":core-security").projectDir = file("shared/core-security")
project(":core-notifications").projectDir = file("shared/core-notifications")
project(":core-sync").projectDir = file("shared/core-sync")
project(":feature-pairing").projectDir = file("shared/feature-pairing")
project(":feature-hearttoheart").projectDir = file("shared/feature-hearttoheart")
project(":feature-calls").projectDir = file("shared/feature-calls")
project(":feature-playback").projectDir = file("shared/feature-playback")
project(":feature-study").projectDir = file("shared/feature-study")
