#!/usr/bin/env bash
set -e
git clone https://github.com/MichaelSnowden/unit_converter
cd unit_converter
mvn generate-sources
sqlite3 unit_converter.db < init.sql
mvn clean compile assembly:single
echo "#!/usr/bin/env bash" >> unit_converter.sh
echo "java -jar $(pwd)/target/unit_converter.jar \$1" >> unit_converter.sh
sudo chmod 777 unit_converter.sh
sudo cp unit_converter.sh /usr/bin/unit_converter
echo "You can now type 'unit_converter' from any directory to open the REPL"
echo "Installation complete! We will now open the REPL"
unit_converter