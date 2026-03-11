##Gotta download the cfr-0.152.jar file first, then run the following commands

jar xf "ATM System - Version 1.0.jar"
# Create a dedicated folder for the source code
mkdir Decompiled_Source -ErrorAction SilentlyContinue
# Decompile the JAR and output it to that folder
java -jar cfr-0.152.jar "ATM System - Version 1.0.jar" --outputdir Decompiled_Source
