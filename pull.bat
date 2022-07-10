@echo off
mkdir spigot

cd "./spigot"
mkdir plugins

powershell -Command "Invoke-WebRequest https://download.getbukkit.org/spigot/spigot-1.18.1.jar -OutFile ./spigot.jar"
powershell -Command "Invoke-WebRequest https://dev.bukkit.org/projects/worldedit/files/latest -OutFile ./plugins/WorldEdit.jar"

echo "Starting a server now. You'll have to edit eula.txt to accept Mojang's EULA."
echo "After doing so, double-click spigot.jar again and the server should run."
echo "Drop the plugin into your Plugins folder once generated, restart the server and you're good to go!"

start spigot.jar
