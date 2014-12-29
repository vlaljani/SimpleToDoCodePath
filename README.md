SimpleToDoCodePath
==================

This is an Android demo application for a simple to do list. 
See <a href="https://docs.google.com/presentation/d/15JnmfmFa0hJOEkBhG_TeymChLzDzpOTJvBlOj29A9fY/edit#slide=id.gf45d6347_3_0">this</a> for a step-by-step tutorial of task addition, removal, saving to file. 
See <a href="http://courses.codepath.com/snippets/intro_to_android/prework">this</a> for guidance on task editing. 
See <a href="http://guides.codepath.com/android/Using-Intents-to-Create-Flows">this</a> for a quick tutorial on Intents. 

Time spent (on app excluding setup, and first walkthrough): ~3 hours 

Completed user stories:

Required: User can successfully add items using an add item button

Required: User can successfully remove an item by long click on an item

Required: User can edit a todo item

Required: App persists todo items and retrieves them on app restart

Extensions - Completed user stories:
-----------------------------------

Suggested: The app persists todo items and retrieves them from the SQLLite DB instead of a text file using the ActiveAndroid ORM

Suggested: The app uses Cursor Adapter instead of Array Adapter

Suggested: Improved the style of the todo items using CustomCursorAdapter

Suggested: Use a DialogFragment with a Save button instead of a new Activity for EditItem 

Suggested: Adding priorities for to do items. 

Priorities are picked using number pickers, which are available for range 0 - NumberOfTasks+1. They can be edited in the dialog fragment. They are indicated to the left of the to do item using exclamation points (!) in red for the top 3 priorities, and in yellow using P followed by the priority number after. Selecting priority 0 => no priority. 

Suggested: Support for completion due dates, which are listed in the list view with items. 

Support for marking tasks as complete, so they appear with a strikethrough in the list.

Updated GIF:
------------

Pre-work GIF:
-------------
![alt tag](https://github.com/vlaljani/SimpleToDo/blob/master/SimpleToDo.gif)
