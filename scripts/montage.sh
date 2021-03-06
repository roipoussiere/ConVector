#!/bin/sh

export MAGICK_THREAD_LIMIT=1 MAGICK_MEMORY_LIMIT=256MiB
ROOT="$(dirname $(dirname $(readlink -f "$0")))"
cd "$(dirname $(readlink -f "$1"))"
FILE="$(basename "$1")"
mkdir -p pdf ps miff diff montage svg
java -ea -jar "$ROOT/convector.jar" "$FILE" "svg/$FILE.svg" "ps/$FILE.ps" "pdf/$FILE.pdf"
convert +antialias "$FILE" "miff/$FILE.miff"
ALL="miff/$FILE.miff svg/$FILE.svg diff/$FILE.miff"
SCORE="$(compare -alpha off -metric MSE $ALL 2>&1 | cut -d' ' -f1 | cut -d. -f1)"
printf "%5d %s\n" "$SCORE" "$FILE"
[ "$SCORE" -lt 256 ] || ! \
montage -background white $ALL "ps/$FILE.ps" "pdf/$FILE.pdf" -geometry 533x450 "montage/$FILE.png"
