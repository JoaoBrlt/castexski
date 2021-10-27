#!/usr/bin/env bash

case "$(uname -s)" in

   CYGWIN*|MINGW*|MSYS*)
     csc -out:server.exe -recurse:*.cs -r:Newtonsoft.Json.dll -lib:src;;


   *)
     mcs -reference:Newtonsoft.Json.dll src/*.cs -pkg:wcf -out:server.exe;;

esac
