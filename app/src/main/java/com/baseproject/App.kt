package com.baseproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // Hilt Activity must be attached to an @AndroidEntryPoint Application
class App : Application()