import SwiftUI
import sharedLibrary

class SubRedditViewModel: ObservableObject {
    let repository = AppGraphCompanion.shared.instance.repository
    
    @Published private(set) var uiState = UiState.Loading

    private(set) var subReddit = "Amoledbackgrounds"
    private(set) var filter = ModelFilter.rising

    func fetchWallpapers() {
        uiState = UiState.Loading
        repository.getWallpapersAsync(subreddit: subReddit, filter: filter) { result, error in
            DispatchQueue.main.async {
                if let result = result {
                    self.uiState = UiState.ShowContent(feed: result)
                } else {
                    self.uiState = UiState.ShowError(msg: error?.localizedDescription ?? "error")
                }
            }
        }
    }

    func loadMoreAfter(afterCursor: String) {
        repository.fetchMoreWallpapersAfter(subreddit: subReddit, filter: filter, afterCursor: afterCursor) { (_, error) in
            if (error != nil) {
                self.uiState = UiState.ShowError(msg: error?.localizedDescription ?? "error")
            } else {
                DispatchQueue.main.async {
                    self.fetchWallpapers()
                }
            }
        }
    }

    func changeSubReddit(subReddit: String) {
        self.subReddit = subReddit
        self.fetchWallpapers()
    }

    func changeFilter(filter: ModelFilter) {
        self.filter = filter
        self.fetchWallpapers()
    }
    
    enum UiState {
        case Loading
        case ShowError(msg: String)
        case ShowContent(feed: ModelFeed)
    }
}

class ImageLoader: ObservableObject {
    @Published var data: Data? = nil

    init(urlString:String) {
        guard let url = URL(string: urlString) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data else { return }
            DispatchQueue.main.async {
                self.data = data
            }
        }.resume()
    }
}
