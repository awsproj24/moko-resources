#
# Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
#

set -e

log() {
  echo "\033[0;32m> $1\033[0m"
}

./gradlew clean assembleDebug
log "compose-resources-gallery android success"

./gradlew clean jvmJar
log "compose-resources-gallery jvm success"

./gradlew clean jsBrowserDistribution --rerun-tasks
log "compose-resources-gallery js success"

if ! command -v xcodebuild &> /dev/null
then
    echo "xcodebuild could not be found, skip ios checks"

    ./gradlew build
    log "compose-resources-gallery full build success"
else

    ./gradlew clean compileKotlinIosX64
    log "compose-resources-gallery ios success"

    ./gradlew clean podspec build generateDummyFramework
    log "compose-resources-gallery full build success"

    (
    cd iosApp &&
    pod install &&
    set -o pipefail &&
    xcodebuild -scheme iosApp -workspace iosApp.xcworkspace -configuration Debug -destination "generic/platform=iOS Simulator" build CODE_SIGNING_REQUIRED=NO CODE_SIGNING_ALLOWED=NO | xcpretty
    )
    log "compose-resources-gallery ios xcode success"
fi

