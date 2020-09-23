# Surface TV
"Surface TV" is an Android tool app that can help user to search TV info by request LineTV drama info API.

It provides the function to search dramas' info, and view detail info by click in the drama list.

The project architecture is composed of the following components:

     * Rest API calls using Retrofit2 libray 
     * DB ORM through ROOM libray 
     * RxJava2 compose the data flow
     * SearchView & RecycleView provide UI

## How it works ?
Together, RxJava2 compose the data flow by organize the Retrofit2 & ROOM process. And users can search drama name by click the SearchView uppon the drama list.

## To Do List

     * Unit Test with LineTV drama info API
     * Try MVVM architecture

