## HTTP head Inspector Project

This projects provides a web site for acquiring the device identifier
from http header.


* _spray-can_, Scala 2.10 + Akka 2.3 + spray 1.3 


Follow these steps to get started:


1. Install scala 2.10, sbt 0.13 by root

        $ ./install.sh

2. Configure config.properties

        # For web server program using
        server.listen=0.0.0.0
        server.port=1357
        run.folder=./run
        
        # For shell script using (scp $file $backend.user@$backend.ip:$backend.folder) 
        # (We don't user password. please put AP's RSA public key to backen server)
        backend.user=brian
        backend.ip=10.20.0.152
        backend.folder=/home/brian

3. Compile and deploy web server

        $ sbt assembly

4. Start web app

        $ ./bin/start.sh

5. Browse to [http://ip:port] which you define in config.properties file

6. Stop the web app:

        $ ./bin/stop.sh

