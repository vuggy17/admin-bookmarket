package com.example.admin_bookmarket.data.common


object Constants {
    //App
    const val TAG = "FireAppTag"

    //Intents
    const val SPLASH_INTENT = "splashIntent"
    const val AUTH_INTENT = "authIntent"
    const val MAIN_INTENT = "mainIntent"

    //Bundles
    const val MOVIE = "movie"

    //Database Types
    const val PRODUCT_NAME = "productName"
    const val CLOUD_FIRESTORE = "Cloud Firestore"
    const val REALTIME_DATABASE = "Realtime Database"

    //References
    const val USERS_REF = "accounts"
    const val BOOK_REF = "books"

    //Fields
    const val NAME = "name"
    const val EMAIL = "email"
    const val PHOTO_URL = "photoUrl"
    const val CREATED_AT = "createdAt"
    const val RATING = "rating"

    //Bindings
    const val MOVIE_POSTER = "moviePoster"
    const val PRODUCT_LOGO = "productLogo"

    enum class ACTIVITY {
       PROFILE, MENU, SEARCH, CART, CATEGORY, FEATURE, CATEGORY_DETAIL;

        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }
    enum class TRANSACTION{
        RECEIVED, COMPLETE,DELIVERING,CANCEL;
        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }

    enum class CATEGORY{
        COMIC, FICTION, NOVEL, BUSINESS, TECHNOLOGY, ART;

        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }

    // activity detail
    const val ITEM = "ITEM_TO_DISPLAY"


    //Tags
    const val VMTAG = "VMTAG"

}