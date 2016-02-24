WebCrawler Project Phase 1

To run the project.
add it to a java application (eclipse)

When the project starts,
it will display a menu with four options
1.start
2.pause
3.resume
4.quit

Note: you cannot pause or resume before the program starts

Once start:
the program will ask for a link

NOTE: the link has to be a validated link, this means including http:// or https:// infront of it.

Example link: 
http://www.google.com  (ok.. maybe not google...)
Then the console will ask for the depth

NOTE: it is recommended to input a depth of less than 5 to avoid out of memory issues...
this recommendation number should changed based on the amount of links a website have
I believe google.com works fine with a depth of 2

Then it will ask for the # of words to match
and ask you all of those words

The application will then crawl the web, this might take a few minute depending on the link.

You also have the option to pause or resume the thread while this is happening.
A menu will show up and enter 2 or 3 to pause or resume the thread.
press 4 to quit

After that is completed, the application will print out the links
and create folders/file for the websites that was visited.

The files/folders can be found on the sitemap folder. 

This sitemap folder will be deleted and recreated when the application starts.
so that way there will be a new start each time.

NOTE: because the console output is super large depending on the depth. 
it is recommended to change the settings on your console.

