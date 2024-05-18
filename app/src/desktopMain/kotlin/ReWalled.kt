import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.di.DesktopAppGraph
import com.bidyut.tech.rewalled.ui.App

fun main() = application {
    AppGraph.assign(
        DesktopAppGraph(
            true,
        )
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "ReWalled",
    ) {
        App()
    }
}
