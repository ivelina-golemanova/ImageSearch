# ImageSearch

Functionallity of the application is to search images for a given word by using the Flickr API.

## Getting Started
The project is separated in two layers - presentation and service. The architectire is based on the MVVM design pattern.

### Presentation layer

The presenatation layer consist of a base clases for Activity and ViewModel. The ```BaseViewModel``` inherits the ```ViewModel``` from the Android Architecture Components. The communication from the view model to the activity is through ```LiveData``` and from 
the activity to the viewModel with direct calls.
There is no Fragments on this stage, because it is a one screen application.

The ```ImageSearchActivity``` supports endless scrolling and pagination. The pagination is implemented with a 
```RecyclerView.OnScrollListener```.

### Service Layer

The presentation layer is dependant on the service layer. To manage the dependencies between these components a Service locator pattern has been used. It is implemented with the help of the ```ServiceLocator``` class.

The API calls are done with an ```AsyncTask```. There is also a simple bitmap caching implemented with the help of the ```LruCache``` class.

## Improvements

It would be good if a disk caching is implemented. Also some improvements on the layout in order to "beautyfy" it and some cool animations can be added.
The UX can be improved with adding some error handling and messages when there is no internet connection.

#### Relaese apk can be found in the project
```app-release.apk```
