import SwiftUI
import ReWalledUI

@main
struct iOSApp: App {
    init() {
        AppGraphCompanion.shared.assign(graph: IosAppGraph())
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
