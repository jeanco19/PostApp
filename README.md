# **Post App**

This application was created to show the user a list of post and their respective details. Also the user can store his favorite posts. The list of posts comes from the [JSONPlaceholder](https://jsonplaceholder.typicode.com/ "JSONPlaceholder") API

#### Architecture

The application uses Clean Architecture and MVVM pattern as recommended by Google in its [architecture guide](https://developer.android.com/topic/architecture "architecture guide"). This architecture help to separate the responsabilties of the layers and make the app more scalable and easy to test.

The app has 3 layers that communicate with each other with the help of dependency injection (DI) in charge of Dagger Hilt. Also, the app use Coroutines and Kotlin Flow to handle the data.

**Data Layer (Datasources and Repositories):** This layer contains the data sources that the app uses, as remote as local. Retrofit library is used for the remote data source and the Jetpack Room Database library is used for the local data source. Also, this layer contains Repositories to manage which data source should be called.

**Domain Layer (Use Cases):** This layer contains the use cases that encapsulate the business logic of the project, like special requirements that project needs.

**Presentation Layer (ViewModels and UI):** This layer contains the ViewModels that transform the data that comes from data layer to UI states. The ViewModels uses a UI State models + StateFlow to refresh the data in the UI. And the UI control the user´s interaction with the application.

#### Third-party libraries

**Retrofit:** This is the common library to handle the server requests, because his implementation is very easy and offer many ways for use. For example create interceptors in the request is very easy. [Retrofit repository](https://github.com/square/retrofit "Retrofit repository").

**Turbine:** This library is very useful to test Kotlin Flows because help to testing the collecting process. For example if you use StateFlow in your ViewModels, this library help you to know in the test if the data return in the right orden in the collect function. [Turbine repository](https://github.com/cashapp/turbine "Turbine repository").

**MockWebServer:** This library is important for testing your API REST, because it provide a way to create a mock of your API REST that helps you to create all the tests that you want without affecting the real server. [MockWebServer repository](https://github.com/square/okhttp/tree/master/mockwebserver "MockWebServer repository").

**Mockito:** This library is very useful to creating instances of classes that you need to provide to other classes in your tests. These instances are copies of the real classes and that is useful in the tests tha you want to do. [Mockito repository](https://github.com/mockito/mockito "Mockito repository").

#### How to run the app?

Fist you need clone this repository and compile the app on your device. After that, open the app on your device and it will show you the list of posts. If you want to see the details of a post, you must click on the post that you want to see and you will be navigate to the details screen. On this screen, you can select that post as your favorite. After doing that, the post will also show up on the favorites list screen.

In this favorites sreen you will have your favorite post and you can add more posts or delete ones you want.

Also in the post list you can delete each post or delete all posts from the list. And if you want see a new post, you can refresh the list by a swiping on the list.