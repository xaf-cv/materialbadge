# MaterialBadge [![Download](https://api.bintray.com/packages/xaf/maven/MaterialBadge/images/download.svg)](https://bintray.com/xaf/maven/MaterialBadge/_latestVersion)

MaterialBadge is a small, RTL-ready Android library which simplifies equipment of Views with nice badges of various shapes, sizes, colors and labels

## Screenshot

![alt text](https://github.com/0x0af/materialbadge/blob/master/Screenshot_20160227-062627.png "Screenshot")

## Demo

[Material Badge Example in Play Store](https://play.google.com/store/apps/details?id=xaf.clean.materialbadgeexample)

# How does it work?

Wrap your View with the BadgedView class (which extends the FrameLayout class) to display a customizable badge

## Include via XML layout

    <xaf.clean.materialbadge.BadgedView
        android:id="@+id/badgedView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:badgeIsVisible="true"
        app:badgeText="1"
        app:badgeTextColor="#FFFFFF"
        app:badgeTextSize="10sp"
        app:badgeColor="#FF0000"
        app:badgeSize="12dp"
        app:badgeOffsetX="4dp"
        app:badgeOffsetY="0dp"
        app:badgePosition="end|top"
        app:badgeShape="circle">

        <!-- Put a View to be equipped with a badge here -->
        <View
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </xaf.clean.materialbadge.BadgedView>
    
## Customize via XML attributes

| Attribute          | Value                                                                         |
|--------------------|-------------------------------------------------------------------------------|
| app:badgeIsVisible | boolean, defines badge visibility                                             |
| app:badgeText      | string, defines badge text                                                    |
| app:badgeTextColor | color, defines badge text color                                               |
| app:badgeTextSize  | dimension, defines badge text size                                            |
| app:badgeColor     | color, defines badge (body) color                                             |
| app:badgeSize      | dimension, defines badge size (diameter for circle, side for square and rhomb)|
| app:badgeOffsetX   | dimension, defines x-axis offset from the corner selected with badgePosition  |
| app:badgeOffsetY   | dimension, defines y-axis offset from the corner selected with badgePosition  |
| app:badgePosition  | flag, defines the corner of the child View to which the badge sticks          |
| app:badgeShape     | enum, defines the shape of the badge (circle, square, rhomb)                  |

## Access, customize, update any feature at runtime

    BadgedView badgedView = (BadgedView) findViewById(R.id.badgedView);
    badgedView.setBadgeText("7");

# Include in your project

Add to your build.gradle `dependencies` closure:

    compile 'xaf.clean:materialbadge:1.1'

# License

    Copyright 2016 Anton F. [A.K.A. xAF]
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
