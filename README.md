# New Player Quiz Plugin

# This plugin manages a quiz for new players. There are locations defined in a .json file. All locations must be in one world. Every location has a x-z size given. When a player moves to a location from the file the plugin will take action depending on the type of the location:
# 1. Information: Send an information message to the player
# 2. Question: Initiate a conversation and teleport the player to a location depending on answers given by the player during conversation
# 3. Teleportation: Teleport the player to a Location and sends a broadcast message.
#
# Commands:
# /npq load [filename]
# Loads QuizData from the file filename.json in the plugin data folder. When no filename is specified loads QuizData.json
# 
# /npq delay tics
# Sets the delay of the broadcast message after a player teleportation in server tics 
# 
# /npq debug [world|here|on|off|me]
# Manages debug output:
# on|off switches debug output on/off
# me directs all debug output to the command sender(player or server console)
# world checks if the quiz is located in the same world as the command sender (player only)
# here checks if the location of the command sender is a location from the quiz