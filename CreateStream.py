import cgi
import os
import urllib
import jinja2
import webapp2

from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users
from Connexus import User, stream_key

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Stream(ndb.Model):
    """Models a Guestbook entry with an author, content, avatar, and date."""
    name = ndb.StringProperty()
    author = ndb.StringProperty()
    tags = ndb.StringProperty(repeated=True)
    picture = ndb.BlobProperty(repeated=True)
    cover = ndb.BlobProperty()
    num_of_pics = ndb.IntegerProperty()
    date = ndb.DateTimeProperty(auto_now_add=True)


DEFAULT_STREAM_NAME = 'default_stream'
class CreateNewStream(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        template_values = {
            'user': user,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('create.html')
        self.response.write(template.render(template_values))

    def post(self):

        #Create a new Stream
        newStream = Stream(parent = users.get_current_user().email())

        newStream.name = self.request.get('stream_name')
        newStream.tags = self.request.get_all('stream_tags')
        #Add a cover for the stream
        cover = self.request.get('cover_image')
        cover = images.resize(cover, 32, 32)
        newStream.cover = cover
        newStream.num_of_pics = 0
        if users.get_current_user():
            newStream.author = User(
                    identity=users.get_current_user().user_id(),
                    email=users.get_current_user().email())

        newStream.content = self.request.get('content')
        # Add the new stream to the datastore
        stream_key = newStream.put()
        # put the new stream into the user's owned stream
        user = self.request.get('user')
        user.streams_owned.append(stream_key)

        self.redirect('/')


# [END CreateNewStream_page]

class Image(webapp2.RequestHandler):
    def get(self):
        greeting_key = ndb.Key(urlsafe=self.request.get('img_id'))
        greeting = greeting_key.get()
        if greeting.avatar:
            self.response.headers['Content-Type'] = 'image/png'
            self.response.out.write(greeting.avatar)
        else:
            self.response.out.write('No image')






