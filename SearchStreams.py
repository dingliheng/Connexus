__author__ = 'yusun'
import cgi
import urllib
import webapp2
import CreateStream
from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users


class SearchStreams(webapp2.RequestHandler):
    def post(self):
        stream_name = self.request.get('stream_name')
        greeting = CreateStream.Greeting(parent=CreateStream.stream_key(stream_name))

        if users.get_current_user():
            greeting.author = users.get_current_user().nickname()

        greeting.content = self.request.get('content')

        # Get image data
        avatar = self.request.get('img')
        # Transform the image
        avatar = images.resize(avatar, 32, 32)
        greeting.avatar = avatar

        greeting.put()

        self.redirect('/?' + urllib.urlencode(
            {'stream_name': stream_name}))