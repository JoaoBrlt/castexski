#!/usr/bin/env bash

if [ ! -f "server.exe" ]; then
    bash compile.sh
fi

FILE=`compgen -G version`
OG_VERSION=1.0
VERSION=1.0
if test -f "$FILE"; then
    OG_VERSION=$(head -n 1 $FILE)
    VERSION=$(bc <<< $OG_VERSION+0.01)
fi

SEARCH="<version>.*<\/version>"
REPLACE="<version>$VERSION<\/version>"

sed -i "s/$SEARCH/$REPLACE/g" pom.xml

sed -i "s/.*/$VERSION/g" $FILE

printf "\n" | jfrog rt upload server.exe team-f-libs-release-local/fr/unice/polytech/isa/weather-service/$VERSION/weather-service-$VERSION.exe --build-name=weather-service --build-number=$VERSION --url=http://vmpx06.polytech.unice.fr/artifactory/ --user=jenkins --password='Y9?5DfVZC=`#^^C$m##^4VuP>'

printf "\n" | jfrog rt upload pom.xml team-f-libs-release-local/fr/unice/polytech/isa/weather-service/$VERSION/weather-service-$VERSION.pom --build-name=weather-service --build-number=$VERSION --url=http://vmpx06.polytech.unice.fr/artifactory/ --user=jenkins --password='Y9?5DfVZC=`#^^C$m##^4VuP>'

printf "\n" | jfrog rt bp weather-service $VERSION --url=http://vmpx06.polytech.unice.fr/artifactory/ --user=jenkins --password='Y9?5DfVZC=`#^^C$m##^4VuP>'
