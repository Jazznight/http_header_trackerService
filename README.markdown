## HTTP head Inspector Project

This projects provides a web site for acquiring the device identifier
from http header.


* _spray-can_, Scala 2.10 + sbt 0.13 + Akka 2.3 + spray 1.3


Follow these steps to get started:


1. Install scala 2.10, sbt 0.13 by root

        $ ./install.sh

2. Generate the default config.properties file and compile the source code.
   (This part will also write some env variable into $HOME/.bashrc, it will be use by cron job)

        $ ./setup.sh

3. Configure config.properties for replacing default generated

        project.folder=/home/andaman/inspector_project

        # For web server program using
        server.listen=0.0.0.0
        server.port=1357
        data.folder=/home/andaman/inspector_project/data
        
        # For shell script using (scp $file $backend.user@$backend.ip:$backend.folder) 
        # (We don't user password. please put AP's RSA public key to backen server)
        backend.user=andaman
        backend.ip=10.20.0.152
        backend.folder=/home/andaman

4. Start web app

        $ start.sh

5. Browse to [http://${server.ip}:${server.port}] which you define in config.properties file

6. Stop the web app:

        $ stop.sh

7. Add bin/scp.sh to crontab for sync data to backend server (suggest every 15 min)

