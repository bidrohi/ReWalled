import SwiftUI
import sharedLibrary

struct PhotoGrid: View {
    private let wallpapers: [ModelWallpaper]
    private let hasMore: Bool
    private let onLoadMore: (() -> Void)

    private let layout = [
        GridItem(.adaptive(minimum: 150))
    ]

    init(
        _ wallpapers: [ModelWallpaper],
        hasMore: Bool,
        onLoadMore: @escaping (() -> Void)
    ) {
        self.wallpapers = wallpapers
        self.hasMore = hasMore
        self.onLoadMore = onLoadMore
    }

    var body: some View {
        let screenWidth = UIScreen.main.bounds.size.width
        let screenHeight = UIScreen.main.bounds.size.height
        let ratio = screenWidth / screenHeight
        ScrollView {
            LazyVGrid(columns: layout) {
                ForEach(wallpapers, id: \.self) { wallpaper in
                    NavigationLink {
                        ZStack {
                            WallpaperCard(wallpaper, requestWidthPx: Int32(screenWidth), imageRatio: ratio, shouldShowPlaceholder: false)
                                .edgesIgnoringSafeArea(.all)
                        } //.preferredColorScheme(.dark)
                    } label: {
                        WallpaperCard(wallpaper, requestWidthPx: 100, imageRatio: ratio, isRounded: true)
                    }
                }
            }.padding()
            if (!wallpapers.isEmpty && hasMore) {
                Button(action: {
                    onLoadMore()
                }) {
                    Text("Load More")
                        .padding(10)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10.0)
                                .stroke(lineWidth: 2.0)
                        )
                }
            }
        }
    }
}

struct WallpaperCard: View {
    @ObservedObject var imageLoader:ImageLoader

    private let wallpaper: ModelWallpaper
    private let ratio: CGFloat
    private let isRounded: Bool
    private let shouldShowPlaceholder: Bool


    init(
        _ wallpaper: ModelWallpaper,
        requestWidthPx: Int32 = 100,
        imageRatio: CGFloat = 10/16,
        isRounded: Bool = false,
        shouldShowPlaceholder: Bool = true
    ) {
        self.wallpaper = wallpaper
        self.ratio = imageRatio
        self.isRounded = isRounded
        self.shouldShowPlaceholder = shouldShowPlaceholder
        imageLoader = ImageLoader(urlString: wallpaper.getUriForSize(width: requestWidthPx))
    }

    var body: some View {
        Rectangle()
            .aspectRatio(ratio, contentMode: .fill)
            .overlay(imageOverlay())
            .cornerRadius(8)
            .clipped()
    }

    @ViewBuilder func imageOverlay() -> some View {
        if let data = imageLoader.data {
            Image(uiImage: UIImage(data: data) ?? UIImage())
                .resizable()
                .scaledToFill()
        } else {
            Color.accentColor
        }
    }
}

extension RoundedRectangle {
    @ViewBuilder
    func `if`<Transform: View>(_ condition: Bool, transform: (Self) -> Transform) -> some View {
        if condition { transform(self) }
        else { self }
    }
}
