import cgi
import os
import urllib
import jinja2
from google.appengine.api import urlfetch
import webapp2

from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users
from Connexus import User
from google.appengine.api import mail

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

def user_key(user_id):
    """Constructs a Datastore key for a Guestbook entity.
    We use guestbook_name as the key.
    """
    return ndb.Key('User', user_id)

class Stream(ndb.Model):
    """Models a Guestbook entry with an author, content, avatar, and date."""
    name = ndb.StringProperty()
    author = ndb.StringProperty()
    tags = ndb.StringProperty(repeated=True)
    # picture = ndb.BlobProperty(repeated=True)
    blob_key = ndb.BlobKeyProperty(repeated=True)
    cover = ndb.StringProperty()
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
            'user_id': user.user_id(),
            'url': url,
            'url_linktext': url_linktext,

        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/CreateStream.html')
        self.response.write(template.render(template_values))

    def post(self):
        self.response.write("qqqqqq")
        user = users.get_current_user()
        if user:

            getUser = User.query(User.email == user.email())
            # self.response.write(str(getUser.fetch(1)))
            global currentUser
            if getUser.fetch(1):
                currentUser = getUser.fetch(1)[0]

            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()

            #Create a new Stream
            newStream = Stream(parent = user_key(currentUser.identity))

            newStream.name = self.request.get('stream_name')
            newStream.tags = self.request.get_all('stream_tags')
            #Add a cover for the stream
            cover_url = self.request.get('cover_image')
            newStream.cover = cover_url
            # Set the num of images to 0
            newStream.num_of_pics = 0
            if users.get_current_user():
                newStream.author = currentUser.email

            newStream.content = self.request.get('content')
            # Add the new stream to the datastore
            stream_key = newStream.put()
            # put the new stream into the user's owned stream

            currentUser.streams_owned.append(stream_key)
            currentUser.put()
            # Start to send invitation emails to friends!
            to_addrs = self.request.get("invitation_emails").split( )
            for email in to_addrs:
                if not mail.is_email_valid(email):
                    # Return an error message...
                    pass
                message = mail.EmailMessage()
                message.sender = user.email()
                message.to = email
                message.body = self.request.get("invatation_message")
                message.send()

            self.redirect('/')

        else:
            login_url = users.create_login_url(self.request.path)
            self.redirect(login_url)
            return



# [END CreateNewStream_page]








