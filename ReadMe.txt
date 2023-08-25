1. Your app should be system app.

2. In order to use hidden system API, we need to include framework.jar from AOSP.
The framework.jar in app/libs/ is copied from AOSP/out/target/common/obj/JAVA_LIBRARIES/framework_intermediates/classes.jar
We can rename this file to framework.jar 

You can refer to the build.gradle in PermissionManager and PermissionManager/app on how to include framework.jar
