#
# Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
#

set -e

log() {
  echo "\033[0;32m> $1\033[0m"
}

./gradlew clean build
log "kotlin-ios-app gradle build success"

if ! command -v xcodebuild &> /dev/null
then
    log "xcodebuild could not be found, skip ios checks"
    log "kotlin-ios-app check is skipped"
else
    (
    cd xcode-project &&
    set -o pipefail &&
    xcodebuild -scheme TestKotlinApp -project TestProj.xcodeproj -configuration Debug -destination "generic/platform=iOS Simulator" build CODE_SIGNING_REQUIRED=NO CODE_SIGNING_ALLOWED=NO | xcpretty
    )
    log "kotlin-ios-app ios xcode success"
fi
