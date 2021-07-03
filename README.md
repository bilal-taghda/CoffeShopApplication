# CoffeShopApplication
android kotlin project + architecture components + testing

## Description
sample MVVM project where i used stateflow instead of livedata and unit tested it with [striKt](https://strikt.io/). 
used hilt DI help me to test ui components such as fragments, activity, recycler view and several views with espresso and mockitto. 
used retrofit as REST API client and gson to get images from [unsplash api](https://unsplash.com/) and show them by glide. 
store data by room and unit tested. 

### Installing

* CLONE the repo
* create gradle.properties file in the project folder and add those lines: 

API_KEY = "[get api key(https://unsplash.com/developers)]"

android.useAndroidX=true

android.enableJetifier=true

### CAN you show me some code? OK :

    @Test
    fun insertShoppingItem() = runBlockingTest {


        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)


        val allShoppingItems = dao.observeAllShoppingItems().first()


       /// assertThat(allShoppingItems).contains(shoppingItem)


        expect {
            that(allShoppingItems){
                contains(shoppingItem)
            }
        }


    }


@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}

from [ImagePickFragmentTest.class](app/src/androidTest/java/com/taghda/coffeshopapplication/ui/ImagePickFragmentTest.kt) : 

    @Test
    fun clickImage_popBackStackAndSetImageUrl() = runBlockingTest{
        val navController = mock(NavController::class.java)
        val imageUrl = "TEST"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<ImagePickFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            viewModel = testViewModel
        }


        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                click()
            )
        )


        verify(navController).popBackStack()
      //  assertThat(testViewModel.curImageUrl.getOrAwaitValue()).isEqualTo(imageUrl)


        expectThat(testViewModel.curImageUrl.first()){
            isEqualTo(imageUrl)
        }
    }
    
    
## Author

[@bilaltaghda in twitter](https://twitter.com/BilalTaghda)

[@bilaltaghda in linkedin](https://www.linkedin.com/in/bilal-taghda-7892b9200/)

[@bilaltaghda5 in facebook](https://www.facebook.com/bilaltaghda5)
