#!/usr/bin/env bash

case "$(uname -s)" in
  # Windows.
  CYGWIN*|MINGW*|MSYS*)
    csc -out:server.exe -recurse:*.cs;;

  # Others.
  *)
    mcs src/*.cs -pkg:wcf -out:server.exe;;
esac
