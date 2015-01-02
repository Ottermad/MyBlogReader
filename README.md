MyBlogReader
============

This is a simple Android application to allow users to read [my blog](https://attwoodthomas.me/show) on their phones
with using a web browser. It fetches the id, title and body of all my posts from the JSON data found at 
[https://attwoodthomas.me/show](https://attwoodthomas.me/show). It then displays all the titles in a list view and
each item in the list view can be tapped to load the content of the post. The posts are formatted with markdown
so I used [this libary](https://github.com/Gunio/MarkdownView) to render the markdown.
