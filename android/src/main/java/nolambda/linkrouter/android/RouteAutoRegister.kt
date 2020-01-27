package nolambda.linkrouter.android

import android.content.Context
import android.util.Log

typealias NameResolver = (String) -> String

class RouteAutoRegister(
    private val plugin: RouterPlugin,
    private val nameResolver: NameResolver = { name -> "nolambda.init.route.${name}" }
) {

    companion object {
        private const val TAG = "RouteAutoRegister"
    }

    fun registerScreenIfNeeded(route: BaseRoute<*>) {
        if (plugin.isUseAnnotationProcessor.not()) return
        val name = "${route.javaClass.simpleName}RouteInit"
        val fullClassName = nameResolver(name)
        try {
            val routeInit = Class.forName(fullClassName)
            routeInit.getDeclaredConstructor(Context::class.java).newInstance(plugin.appContext)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "No initialization found for $fullClassName")
        }
    }
}