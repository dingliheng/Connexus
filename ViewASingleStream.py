from Connexus import User
from CreateStream import Stream

__author__ = 'yusun'
import cgi
import urllib
import webapp2

from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users


class ViewStream(webapp2.RequestHandler):
    def post(self):
        stream_name = self.request.get('stream_name')
        stream = Stream.query(Stream.name == stream_name)
        # user = User.query(User.email == users.get_current_user().email)
        # Check if the user logs in
        user = User.query(User.email == users.get_current_user().email)
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'


        # Get image data
        image = self.request.get('img')
        # Transform the image
        image = images.resize(image, 32, 32)
        stream.picture.append(image)

        self.redirect('/?' + urllib.urlencode(
            {'stream_name': stream_name}))

# [START subsribe this stream]

class Subsribe(webapp2.RequestHandler):
    def post(self):
         user = User.query(User.email == users.get_current_user().email)
         if user:
            stream = self.request.get('stream')
            user.fetch(1)[0].stream_subsribed.append(stream.key())
            self.response.out.write('Subsribe success!')
         else:
             self.redirect(users.create_login_url(self.request.uri))
