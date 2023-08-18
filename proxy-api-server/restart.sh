(cd /home/ubuntu/projects/23_HF056/proxy-api-server && ./gradlew build -x test)

sudo kill -9 `sudo lsof -t -i:8090`

(cd /home/ubuntu/projects/23_HF056/proxy-api-server/build/libs && nohup java -jar hanium.proxyapiserver-0.0.1-SNAPSHOT.jar &)

disown
