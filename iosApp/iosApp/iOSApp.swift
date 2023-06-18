import SwiftUI
import compose

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)
        let mainViewController = IOSContentKt.MainViewController(window: window!)
        window?.rootViewController = mainViewController
        window?.makeKeyAndVisible()
        return true
    }

     func application(
        _ application: UIApplication,
        supportedInterfaceOrientationsFor supportedInterfaceOrientationsForWindow: UIWindow?
     ) -> UIInterfaceOrientationMask {
         return UIInterfaceOrientationMask.all
    }
}
