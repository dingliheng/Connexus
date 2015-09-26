import cgi
import logging
from Connexus import User
from CreateStream import Image, CreateNewStream
import ViewASingleStream

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

        user = users.get_current_user()
        owned_streams = []
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())

            # self.response.write(str(getUser.fetch(1)))
            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]

                self.response.write(currentUser)
                # Get the keys of streams
                streams_key = currentUser.streams_owned
                self.response.write(streams_key)
                for stream_key in streams_key:
                    stream = stream_key.get()
                    owned_streams.append(stream)

            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        # for stream in currentUser.streams_owned:
        template_values = {
            'streams': owned_streams,
            'user_id': currentUser.identity,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/management.html')
        self.response.write(template.render(template_values))


    def post(self):

        # self.request.get('delete')
        self.response.write('pppppp')



app = webapp2.WSGIApplication([('/', MainPage),
                               ('/img', Image),
                               ('/create', CreateNewStream),
                               ('/view', ViewASingleStream.ViewStream)
                               ], debug=True)