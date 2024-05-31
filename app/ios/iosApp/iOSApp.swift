import FirebaseCore
import FirebaseAnalyticsWithoutAdIdSupportTarget
import FirebaseCrashlytics
import FirebasePerformance
import NSExceptionKtCrashlytics
import ReWalledUI
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()

        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
        NSExceptionKt.addReporter(.crashlytics(causedByStrategy: .append))

        Performance.sharedInstance().isInstrumentationEnabled = true

        AppGraph.Companion.shared.assign(graph: IosAppGraph(iOSCoordinator(), enableDebug: true))

        return true
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
