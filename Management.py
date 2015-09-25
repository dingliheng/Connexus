import cgi
from Connexus import User
from CreateStream import Image, CreateNewStream

__author__ = 'yusun'
import os
import jinja2
import webapp2
from google.appengine.api import images
from google.appengine.api import users
from google.appengine.ext import ndb


JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class LoginHandler(webapp2.RequestHandler):
    def get(self):
        template = JINJA_ENVIRONMENT.get_template('/htmls/login.html')
        outstr = template.render({})
        self.response.write(outstr)


    def post(self):
        self.redirect(users.create_login_url(self.request.uri))


class MainPage(webapp2.RequestHandler):
    def get(self):
        # Checks for active Google account session

        user = User.query(User.email == users.get_current_user().email)
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        currentUser = user.fetch(1)[0]
        # Get the keys of streams
        streams_key = currentUser.streams_owned

        owned_streams = []
        for stream_key in streams_key:
            stream = stream_key.get()
            owned_streams.append(stream)

        # for stream in currentUser.streams_owned:
        template_values = {
            'streams': owned_streams,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('management.html')
        self.response.write(template.render(template_values))


    def post(self):
        self.request.get('delete')




app = webapp2.WSGIApplication([('/', MainPage),
                               ('/img', Image),
                               ('/create', CreateNewStream)

                               ], debug=True)