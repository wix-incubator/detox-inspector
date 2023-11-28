[![](https://jitpack.io/v/wix-incubator/detox-inspector.svg)](https://jitpack.io/#wix-incubator/detox-inspector)

## How to integrate the inspector
1. Make sure you have jitpack repository installed. Add the following to your root build.gradle file at the end of repositories:
```groovy
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the following dependency to your app/build.gradle
```groovy
	dependencies {
		implementation 'com.github.wix-incubator:detox-inspector:Tag'
	}
```

## How to invoke the inspector
You have two options to invoke the inspector:
1. adb: `adb shell am start -n {YOUR_PACKAGE_NAME}/com.wix.detox.inspector.features.inspect.InspectActivity`
2. Quick Settings: Open your application, open the quick settings panel (Located at near the Wifi toggle), click on edit, and drag the "Detox Inspector" tile to the quick settings panel. Click on the tile to open the inspector.
