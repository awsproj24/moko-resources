#
# Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
#

set -e

log() {
  echo "\033[0;32m> $1\033[0m"
}

if ! command -v xcodebuild &> /dev/null
then
    log "xcodebuild could not be found, skip ios checks"
    log "ios-static-xcframework check is skipped"
else
    ./gradlew clean build assembleMultiPlatformLibraryXCFramework
    log "ios-static-xcframework gradle build success"

    (
    cd ios-app &&
    set -o pipefail &&
    xcodebuild -scheme TestStaticXCFramework -project TestProj.xcodeproj -configuration Debug -destination "generic/platform=iOS Simulator" build CODE_SIGNING_REQUIRED=NO CODE_SIGNING_ALLOWED=NO | xcpretty
    )
    log "ios-static-xcframework ios xcode success"
fi
