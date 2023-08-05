(cd /home/ubuntu/projects/23_HF056/server && ./gradlew build -x test)

sudo kill -9 `sudo lsof -t -i:8080`

(cd /home/ubuntu/projects/23_HF056/server/build/libs && nohup java -jar api-platform-0.0.1-SNAPSHOT.jar &)

disown
