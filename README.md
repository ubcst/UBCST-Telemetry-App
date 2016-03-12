# UBCST-Telemetry-App
Telemetry app for Android 5.0 Lollipop

### Developing in Linux
To develop in Linux you can install Android Studio.

If you want to view the Android log without installing Android Studio, you can use `adb`.

You can install `adb` by running:
`sudo apt-get install android-tools-adb`

Once `adb` is installed, run the following:
`adb logcat`

You can specify the app by adding the class' TAG added to the above command. For example, if you have a TAG value of "MAIN_ACTIVITY", you would run the following:
`adb logcat -s "MAIN_ACTIVITY"`
