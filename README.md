## THIS PROJECT IS CURRENTLY UNMAINTAINED
### If you wish to help maintain this project please submit an issue and tag me.

### Equally, if you wish to become a maintainer on this project. Let me know

# MultiContactPicker

[![](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#JELLY_BEAN"><img src="https://img.shields.io/badge/API-16%2B-blue.svg?style=flat" alt="API" /></a> 

A simple, material design multi-contact picker using RecyclerView and Alphabetical Fast Scrolling! The picker will read the device contacts (**REMEMBER**: Runtime permissions for retrieving contacts) and allow you to select 1-many and return them in a convenient list. Great for apps that wish to allow users to import from their contacts and/or invite their friends all at once.

**Note:** The contacts will always have the first letter gmail-style avatars, contact photos are not used at this time.


Screenshot (Default Azure Theme)
:-------------------------
![](https://i.imgur.com/4U7Fg8w.png)


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
    implementation 'com.github.broakenmedia:MultiContactPicker:1.8.5'
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
                            .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                            .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .setTitleText("Select Contacts") //Optional - default: Select Contacts
                            .setSelectedContacts("10", "5" / myList) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                            .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                            .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                            .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out) //Optional - default: No animation overrides
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

### Changelog

```
1.8.4
---
- Activity enter and exit animations can now be set via setActivityAnimations
- Phone numbers are no longer returned as a list of strings. Instead as a PhoneNumber object which includes both the number and number type e.g. "Mobile"
- Updated dependencies
- Updated sample app
---
1.8
---
- You can now limit results to contacts that have an EMAIL, PHONE or BOTH using .limitToColumn(). Limiting columns can reduce loading time
- Now able to specify list loading type. Streaming the results to screen or wait and display all at once using .setLoadingType()
- Added ability to specify a list of contacts that will be pre-selected when opening the picker (List of String or ArrayList of ContactResult)
- Fixed memory leak
- Updated dependencies
- Performance improvements and general bug fixes
- Included pull requests
    - Hide progress when loading completes
    - Added error message when no contacts found
    - Added ability to set picker title
- Updated sample app
---
1.7
---
- Contacts are now returned with their respective Contact ID
- You can now open the picker in CHOICE_MODE_SINGLE or CHOICE_MODE_MULITPLE to select 1 or many contacts
- Tweaked control panel text from Select->Finish
- Added the ability to "Select-All" and "Un-Select All"
- Updated dependencies
- Switched from default Fragments to support.v4.Fragments 
---
1.6
---
- Contacts are now shown asynchronously as they load
- Search results now highlight the query text similar to the default contacts app
- Menu file renamed to prevent conflicts
- Various performance enhancements and bug fixes
```

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
