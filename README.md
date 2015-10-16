# QuizPlugin
player quiz bukkit plugin

    When a player moves to a location which is a question location from the NewPlayerQuiz.json file,
    a Conversation is initiated. I like this more than clicking something for a question, 
    but if listening to PlayerMoveEvents will cause lag this can be changed of course.
    The plugin will preset the question text from the .json file and then one by one the answer texts. 
    If the player typed in all answers as indicated in the .json file, 
    he will be teleported to the success location otherwise to the fail location. 

