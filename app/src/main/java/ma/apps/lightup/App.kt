package ma.apps.lightup

import android.app.Application
import ma.apps.lightup.cache.Cache

class App : Application() {

    companion object {
        lateinit var dimens: Dimens
        lateinit var cache: Cache
    }

    override fun onCreate() {
        super.onCreate()
        dimens = Dimens(this)
        cache = Cache(this)
    }
}