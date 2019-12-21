# WeatherLoggerAutoUpdate
This is a weahter application, which detect your current location, get weather by help of lat and lng, save it to local storage and update every 15 mins.

## Installation
### Step 1
Make sure your installed Android studio with Kotlin support.

### Step 2
Clone this repo to your local machine using [this link](https://github.com/MustafaKhaled/WeatherLoggerAutoUpdate.git)

## Features

### Coroutines
 Through this repo you could discover the power of coroutines in Kotlin. Applying different **scopes** of coroutine (e.g. ViewModelScope, LiveDataScope, etc ...)
check links below for more information and application about coroutines
- https://developer.android.com/topic/libraries/architecture/coroutines 
- https://android.jlelse.eu/coroutine-in-android-working-with-lifecycle-fc9c1a31e5f3

### WorkManager
WorkManager is a part of architectural component, it's very useful to schedule tasks. These tasks maybe PeriodicWorkRequest or OneTimeWorkRequest.
Maybe you need to call a suspended function within the worker so you can use **CoroutineWorker** . check the link below for more about
coroutineWorker.
 - https://developer.android.com/topic/libraries/architecture/workmanager/advanced/worker

### Room Database
Room has support for suspend function in dao. Used Room database as a local storage for weather loggers. check out the link below
- https://ahsensaeed.com/kotlin-corounties-android-room-persistence-suspend/


## Support
Reach out to me at one of the following

Email: mustafakhaledmustafa@outlook.com

LinkedIn: https://www.linkedin.com/in/mustafa-khaled
