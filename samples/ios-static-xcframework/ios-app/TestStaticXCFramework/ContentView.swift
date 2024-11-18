//
//  ContentView.swift
//  TestStaticXCFramework
//
//  Created by Aleksey Mikhailov on 22.01.2022.
//  Copyright © 2022 IceRock Development. All rights reserved.
//

import SwiftUI
import MultiPlatformLibrary

struct ContentView: View {
    var body: some View {
        Text(Testing.shared.getHelloWorld().localized())
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
