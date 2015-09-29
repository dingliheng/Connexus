import cgi
import logging
from Connexus import User
import SearchStreams
import TrendingStreams

from CreateStream import CreateNewStream
import ViewASingleStream
import ViewAllStreams

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
        subscribed_streams = []
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
                # streams_key = currentUser.streams_searched
                # self.response.write(streams_key[0].get().num_of_views+1)
                for stream_key in streams_key:
                    stream = stream_key.get()
                    if stream:
                        owned_streams.append(stream)

                for stream_key in currentUser.streams_subscribed:
                    stream_subscribed = stream_key.get()
                    if stream_subscribed:
                        subscribed_streams.append(stream_subscribed)

            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        # for stream in currentUser.streams_owned:
        template_values = {
            'streams': owned_streams,
            'subscribed_streams': subscribed_streams,
            'user_id': currentUser.identity,
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/management.html')
        self.response.write(template.render(template_values))


    def post(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())
            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]
                # currentUser.streams_owned = []
                # currentUser.put()

                streams_key = currentUser.streams_owned

                for stream_key in streams_key[:]:
                    stream = stream_key.get()
                    # if stream:
                    if self.request.get(str(stream.key.id())):
                            currentUser.streams_owned.remove(stream_key)
                            for stream_key_searched in currentUser.streams_searched:
                                if stream_key_searched == stream_key:
                                    currentUser.streams_searched.remove(stream_key)
                            stream_key.delete()
                currentUser.put()
                currentUser.put()

            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
        self.redirect('/')

app = webapp2.WSGIApplication([('/', MainPage),
                               # ('/img', Image),
                               ('/create', CreateNewStream),
                               ('/subscribe', ViewASingleStream.Subscribe),
                               ('/upload_photo', ViewASingleStream.PhotoUploadHandler),
                               ('/view_photo/([^/]+)?', ViewASingleStream.ViewPhotoHandler),
                               ('/view', ViewASingleStream.ViewStream),
                               ('/search', SearchStreams.SearchStreams),
                               ('/trend', TrendingStreams.TrendStreams),
                               ('/viewall', ViewAllStreams.ViewAllStreams)
                               ], debug=True)