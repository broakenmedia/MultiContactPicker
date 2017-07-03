# MultiContactPicker

[![](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#JELLY_BEAN"><img src="https://img.shields.io/badge/API-16%2B-blue.svg?style=flat" alt="API" /></a> 

A simple, material design multi-contact picker using RecyclerView and Alphabetical Fast Scrolling! The picker will read the device contacts (**REMEMBER**: Runtime permissions for retrieving contacts) and allow you to select 1-many and return them in a convenient list. Great for apps that wish to allow users to import from their contacts and/or invite their friends all at once.

**Note:** The contacts will always have the first letter gmail-style avatars, contact photos are not used at this time.


Screenshot
:-------------------------
![](http://i.imgur.com/6duvoHm.png)


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
    compile 'com.github.xbroak:MultiContactPicker:v1.0'
}
```
### Usage

Open the picker in your activity/fragment:

```java
new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
    .theme(R.style.MyCustomPickerTheme) //Optional - default: Inherits project style
    .hideScrollbar(false) //Optional - default: false
    .showTrack(true) //Optional - default: true
    .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Green
    .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Green
    .textColor(Color.WHITE) //Optional - default: White
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
MultiContactPicker will inherit it's theme from the base project, however you can use a custom theme if you wish:

```xml
<style name="MyCustomTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryDark">@color/primary_dark</item>
    <item name="colorAccent">@color/accent</item>
    <item name="android:textColorPrimary">@color/primary_text</item>
    <item name="android:textColorSecondary">@color/secondary_text</item>
</style>
```

This can then be set in the builder above using **.theme(int)**

### Thanks
MultiContactPicker tilises code from these great libraries:

[FastScroll](https://github.com/L4Digital/FastScroll)

[RoundedLetterView](https://github.com/pavlospt/RoundedLetterView)

[RxContacts2](https://github.com/mirrajabi/rx-contacts2)


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
