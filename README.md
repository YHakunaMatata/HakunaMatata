# HakunaMatata

"Hakuna matata" is a Swahili phrase; translated, it roughly means "No worries".  

In this project we're going to build an app which can make people laugh. It's a platform that collect jokes, funny images and funny videos. Also users can post and share their joke on it. We use facebook as the content storage.

(Required)
* [x] User have to loggin into facebook
* [x] User can view the list of content without login. The content will be separated under three tabs
* [x] There's a favorite list
  * [x] User can add jokes into his favorite list
  * [x] User can remove jokes from favorite list
* [x] User can post content onto the platform
  * [x] User can post a text joke
  * [x] User can post a funny image and add the description
* [x] The data will be stored on a open facebook group
* [x] Add cool animation and style on the app

The following **additional** features are implemented:
* [x] Use [GreenDao](https://github.com/greenrobot/greenDAO) as ORM library to store user favoriate post.
* [x] Use [ElasticDownload](https://github.com/Tibolte/ElasticDownload) as progress bar
* [x] Use [Heterogenous RecyclerView](https://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) for Listing
* [x] Use [Card View](https://developer.android.com/intl/ko/reference/android/support/v7/widget/CardView.html) as list item
* [x] integrate `Facebook SDK` + self implememt [Facebook OAuth Client](https://github.com/YHakunaMatata/HakunaMatata/blob/master/app/src/main/java/com/yahoo/hakunamatata/network/FacebookClient.java) for API access.

## Walkthrough
![Video Walkthrough](walkthrough.gif) 

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used
- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Elastic Download](https://github.com/Tibolte/ElasticDownload) - A cool progress bar
- [GreenDao](https://github.com/greenrobot/greenDAO) - ORM library
- [Round Image View](https://github.com/vinc3m1/RoundedImageView) - make image round
- [Sliding Tab](https://github.com/astuetz/PagerSlidingTabStrip) - pager sliding tab
- [Gson](https://github.com/google/gson) - a library for parsing json in java
- [Facebook SDK](https://developers.facebook.com/docs/android) - facebook android sdk
- [Folder Reside Menu](https://github.com/dkmeteor/Folder-ResideMenu) - a folder reside menu
- [Floating Action Button](https://github.com/futuresimple/android-floating-action-button) - a cool material button


## Developer setup
* For Mac OSX, copy `key/debug.keystore` to `~/.android/`
* Since the app follows facebook development guideline, only the author of HakunaMatata can run the app
