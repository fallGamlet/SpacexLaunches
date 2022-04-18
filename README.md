# SpacexLaunches
android application for display rocket launches of SpaceX


# Task
Create an application which display last launches list, from open source API :
- https://github.com/r-spacex/SpaceX-API
- https://api.spacexdata.com/v4/launches

App contains 2 screens: Launch List and Launch Details screen. 
And for advantage add Favorites list.

Launch List: Should show all past launches of SpaceX A one “launch” cell view should contain:
- Name |  launch.name
- Date | launch.date
- Image one of in launch.links.flickr or in launch.links.patch

Launch Details When user tap on a launch, should  open launch details screen:
It should contain:
- YoutubePlayer on tap it play video
- The description (details)
- Rocket name and Payload mass - Link on wikipedia which redirect on wikipedia page

Favorites  Add the option to ​save the favorite​ Launches. This option should be available in both screen on list and in details launch

Launches marked as favorites will be saved in a local database (of your choice). And they will must be re-displayed after restarting the application. 
