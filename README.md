# MultiContactPicker

[![](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#JELLY_BEAN"><img src="https://img.shields.io/badge/API-16%2B-blue.svg?style=flat" alt="API" /></a> 

A simple, material design multi-contact picker using RecyclerView and Alphabetical Fast Scrolling! The picker will read the device contacts (**REMEMBER**: Runtime permissions for retrieving contacts) and allow you to select 1-many and return them in a convenient list. Great for apps that wish to allow users to import from their contacts and/or invite their friends all at once.

**Note:** The contacts will always have the first letter gmail-style avatars, contact photos are not used at this time.


Screenshot (Default Azure Theme)
:-------------------------
![](http://i.imgur.com/idrbwzL.png)


## Install

Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add this to your module build.gradle

``` gradle
dependencies {
    compile 'com.github.broakenmedia:MultiContactPicker:1.4'
}
```
### Usage

Open the picker in your activity/fragment:

```java
new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                            .hideScrollbar(false) //Optional - default: false
                            .showTrack(true) //Optional - default: true
                            .searchIconColor(Color.WHITE) //Option - default: White
                            .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .showPickerForResult(CONTACT_PICKER_REQUEST);
```

Then listen for results:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == CONTACT_PICKER_REQUEST){
        if(resultCode == RESULT_OK) {
            List<ContactResult> results = MultiContactPicker.obtainResult(data);
            Log.d("MyTag", results.get(0).getDisplayName());
        } else if(resultCode == RESULT_CANCELED){
            System.out.println("User closed the picker without selecting items.");
        }
    }
}
```

### Themeing
MultiContactPicker has a default theme called Azure (Screenshot), however you can use a custom theme if you wish by using both the builder calls above and using styles:

```xml
<style name="MyCustomPickerTheme" parent="MultiContactPicker.Azure">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="mcpToolbarTheme">@style/MyCustomToolbarTheme</item>
    <item name="mcpListPopupWindowStyleTheme">@style/MyCustomToolbarTheme</item>
</style>

<style name="MyCustomToolbarTheme" parent="ThemeOverlay.AppCompat.Light">
    <item name="android:textColorPrimary">@color/colorAccent</item>
    <item name="android:textColorSecondary">@color/colorAccent</item>
</style>
```

This can then be set in the builder above using **.theme(int)**

### Thanks
MultiContactPicker utilises code from these great libraries:

[FastScroll](https://github.com/L4Digital/FastScroll)

[RoundedLetterView](https://github.com/pavlospt/RoundedLetterView)

[RxContacts2 (Heavily Modified)](https://github.com/mirrajabi/rx-contacts2)

[RxAndroid](https://github.com/ReactiveX/RxAndroid)

[MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView)


## License

```
Copyright (c) BroakenMedia 2016-2017

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
