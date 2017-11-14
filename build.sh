#!/usr/bin/env bash

./mvnw clean install -Dmaven.test.skip

cd tooling-project
docker build -t tooling/tooling-project .
cd ..

cd tooling-vacation
docker build -t tooling/tooling-vacation .
cd ..

cd tooling-zuul
docker build -t tooling/tooling-zuul .
cd ..