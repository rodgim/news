## News
News is a sample Android project, using <a href="https://newsapi.org/">The News API</a> for the data, based on MVVM and Clean architecture.

## Features
* 100% Kotlin
* MVVM and Clean architecture
* Android Architecture Components.
* Dependency injection
* Kotlin Coroutines + Flow
* Testing

## Tech Stacks
* [Retrofit](http://square.github.io/retrofit/) + [OkHttp](http://square.github.io/okhttp/) - RESTful API and networking client.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency injection.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - A collections of libraries that help you design robust, testable and maintainable apps.
  * [Room](https://developer.android.com/training/data-storage/room) - Local persistence database.
  * [ViewModel](https://developer.android.com/reference/androidx/lifecycle/ViewModel) - UI related data holder, lifecycle aware.
  * [Navigation component](https://developer.android.com/guide/navigation) - Fragment routing handler.
* [Coroutine](https://developer.android.com/kotlin/coroutines) Concurrency design pattern for asynchronous programming.
* [Flow](https://developer.android.com/kotlin/flow) Stream of value that returns from suspend function.
* [Glide](https://github.com/bumptech/glide) - Image loading.

## Setup project
Get API KEY from [NewsApi](https://newsapi.org/) and edit the news_api_key constant that you can find in gradle.properties.

## LICENSE
```  
Copyright (c) 2023 Rodrigo Gimenez  
  
Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or  
sell copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:  
  
The above copyright notice and this permission notice shall be included in all  
copies or substantial portions of the Software.  
  
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  
SOFTWARE.  
```