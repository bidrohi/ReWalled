import Foundation
import ReWalledUI
import SwiftUI

class iOSCoordinator: PlatformCoordinator {
    func triggerShareIntent(
        context: PlatformContext,
        w: Wallpaper
    ) {
        let url = URL(string: w.url)!
        downloadWallpaper(url) { (destinationUrl: URL?, error: Error?) in
            Task { @MainActor in
                if let url = destinationUrl, let imageToSend = UIImage(contentsOfFile: url.relativePath) {
                    let textToSend = "\(w.summary) by \(w.author) at \(w.postUrl)"
                    let activityViewController = UIActivityViewController(activityItems: [imageToSend, textToSend], applicationActivities: nil)
                    context.viewController.present(activityViewController, animated: true)
                } else {
                    let alertController = UIAlertController(
                        title: "Failed to download",
                        message: "Error: \(error!)",
                        preferredStyle: .actionSheet
                    )
                    alertController.addAction(UIAlertAction(title: "OK", style: .default))
                    context.viewController.present(alertController, animated: true, completion: nil)
                }
            }
        }
    }

    func triggerDownloadIntent(
        context: PlatformContext,
        w: Wallpaper
    ) {
        let url = URL(string: w.url)!
        downloadWallpaper(url) { (destinationUrl: URL?, error: Error?) in
            Task { @MainActor in
                if let url = destinationUrl, let imageToSave = UIImage(contentsOfFile: url.relativePath) {
                    UIImageWriteToSavedPhotosAlbum(
                        imageToSave,
                        nil,
                        #selector(self.onImageSavedToCameraRoll(_:didFinishPhotoLibrarySavingWithError:contextInfo:)),
                        UnsafeMutableRawPointer(Unmanaged.passRetained(context).toOpaque())
                    )
                } else {
                    let alertController = UIAlertController(
                        title: "Failed to download",
                        message: "Error: \(error!)",
                        preferredStyle: .actionSheet
                    )
                    alertController.addAction(UIAlertAction(title: "OK", style: .default))
                    context.viewController.present(alertController, animated: true, completion: nil)
                }
            }
        }
    }

    @objc private func onImageSavedToCameraRoll(
        _ image: UIImage,
        didFinishPhotoLibrarySavingWithError error: Error?,
        contextInfo: UnsafeRawPointer
    ) {
        let context = Unmanaged<PlatformContext>.fromOpaque(contextInfo).takeRetainedValue()
        if let err = error {
            let alertController = UIAlertController(
                title: "Failed to save image",
                message: "Error: \(err)",
                preferredStyle: .actionSheet
            )
            alertController.addAction(UIAlertAction(title: "OK", style: .default))
            context.viewController.present(alertController, animated: true, completion: nil)
        } else {
            let alertController = UIAlertController(
                title: "Wallpaper added",
                message: "Successfully added the wallpaper to gallery",
                preferredStyle: .actionSheet
            )
            alertController.addAction(UIAlertAction(title: "OK", style: .default))
            context.viewController.present(alertController, animated: true, completion: nil)
        }
    }

    private func downloadWallpaper(_ url: URL, completion: @escaping (URL?, Error?) -> Void) {
        let documentsUrl =  try! FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
        let destinationUrl = documentsUrl.appendingPathComponent(url.lastPathComponent)
        print(destinationUrl)

        if FileManager().fileExists(atPath: destinationUrl.path) {
            print("File already exists [\(destinationUrl.path)]")
            completion(destinationUrl, nil)
            return
        }

        let request = URLRequest(url: url)

        let task = URLSession.shared.downloadTask(with: request) { tempFileUrl, response, error in
            if error != nil {
                completion(nil, error)
                return
            }

            if let response = response as? HTTPURLResponse {
                if response.statusCode == 200 {
                    if let tempFileUrl = tempFileUrl {
                        print("download finished")
                        try! FileManager.default.moveItem(at: tempFileUrl, to: destinationUrl)
                        completion(destinationUrl, error)
                    } else {
                        completion(nil, error)
                    }

                }
            }

        }
        task.resume()
    }
}
