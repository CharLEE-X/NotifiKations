// Use `xcodegen` first, then `open ./ComposeMinesweeper.xcodeproj` and then Run button in XCode.
import androidx.compose.ui.window.ComposeUIViewController
import com.charleex.application.RouterContent
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.UIApplicationDelegateProtocolMeta
import platform.UIKit.UIApplicationMain
import platform.UIKit.UIResponder
import platform.UIKit.UIResponderMeta
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow

/**
 * Courtesy of latest examples from
 * https://github.com/JetBrains/compose-jb/tree/59d4e677b61902d7e465b1c6ce24b95379e55271/experimental/examples/falling-balls-mpp
 */
fun main() {
    val args = emptyArray<String>()
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
        autoreleasepool {
            UIApplicationMain(
                argc = argc,
                argv = argv,
                principalClassName = null,
                delegateClassName = NSStringFromClass(SkikoAppDelegate)
            )
        }
    }
}

class SkikoAppDelegate @OverrideInit constructor() : UIResponder(), UIApplicationDelegateProtocol {
    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

    private var _window: UIWindow? = null
    override fun window() = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }

    override fun application(
        application: UIApplication,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        didFinishLaunchingWithOptions: Map<Any?, *>?,
    ): Boolean {
        window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window!!.rootViewController = ComposeUIViewController {
            RouterContent()

        }
        window!!.makeKeyAndVisible()
        return true
    }
}
