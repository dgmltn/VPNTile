#!/usr/bin/env bash
# Usage: check-version-tag.sh <tag>
# Prints the app's versionName when <tag> is "v<versionName>"; fails otherwise.
set -euo pipefail

tag="${1:?usage: check-version-tag.sh <tag>}"
build_file="$(dirname "$0")/../app/build.gradle.kts"
version=$(sed -n 's/^ *versionName = "\(.*\)"$/\1/p' "$build_file")

if [ -z "$version" ]; then
  echo "No versionName found in $build_file" >&2
  exit 1
fi
if [ "$tag" != "v$version" ]; then
  echo "Tag $tag does not match versionName $version in app/build.gradle.kts" >&2
  exit 1
fi
echo "$version"
