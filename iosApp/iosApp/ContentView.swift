import SwiftUI
import FluentIcons
import sharedLibrary

struct ContentView: View {
    @ObservedObject var viewModel = SubRedditViewModel()

    var body: some View {
        NavigationView {
            switch viewModel.uiState {
            case SubRedditViewModel.UiState.Loading:
                VStack{
                    Image(uiImage: UIImage(fluent: FluentIcon.cloudSync48Regular))
                    Text("Loading contents")
                }.onAppear() {
                    viewModel.fetchWallpapers()
                }
            case SubRedditViewModel.UiState.ShowError(let msg):
                VStack{
                    Image(uiImage: UIImage(fluent: FluentIcon.cloudDismiss48Regular))
                    Text("Failed to load contents from the cloud. Error: \(msg)")
                }
            case SubRedditViewModel.UiState.ShowContent(let feed):
                PhotoGrid(feed.wallpapers, hasMore: feed.afterCursor != nil, onLoadMore: {
                    viewModel.loadMoreAfter(afterCursor: feed.afterCursor!)
                }).navigationTitle("/r/\(viewModel.subReddit)")
                    .navigationBarItems(
                        trailing: Menu {
                            // TODO: iterate through Filter.values()
                            let values = [
                                ModelFilter.best,
                                ModelFilter.controversial,
                                ModelFilter.hot,
                                ModelFilter.theNew,
                                ModelFilter.rising,
                                ModelFilter.top,
                            ]
                            ForEach(values, id: \.self) { value in
                                Button(action: {
                                    viewModel.changeFilter(filter: value)
                                }, label: {
                                    Text(value.name)
                                })
                            }
                        } label: {
                            Text(viewModel.filter.name)
                        })
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
