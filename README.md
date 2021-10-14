
# BaseProject

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Bogdan-blue)](https://www.linkedin.com/in/bogdan-dumchev-770a631a7/)

Base project for MVI applications with Hilt & Navigation Components.
This way you don`t need to repeat the same code in different parts of your project over & over.

### Rename Project

1. Rename folder of project.
2. Rename package 'baseproject' in app module.
3. Rename applicationId in config.gradle.
4. Rename rootProject.name in settings.gradle. Tap sync.
5. Rename package attribute in Manifest.
6. Rename app name in strings.xml.
7. Check tag in main_graph (of NavComponent) for any errors. For example com.'baseproject'.view.ui.user.UserFragment
8. Delete Readme.md (if you need).

