import cgi
import logging
from google.appengine.api.images import get_serving_url
from Connexus import User
import MapHandler
import SearchStreams
import TrendingStreams
import Connexus
from CreateStream import CreateNewStream
import CreateStream
import ViewASingleStream
import ViewAllStreams

__author__ = 'yusun'
import os
import jinja2
import webapp2
from google.appengine.api import images
from google.appengine.api import users
from google.appengine.ext import ndb



class Index(webapp2.RequestHandler):
    def get(self):
        template_values = {}
        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/index.html')
        self.response.write(template.render(template_values))



class MainPage(webapp2.RequestHandler):
    def get(self):
        # Checks for active Google account session

        user = users.get_current_user()
        owned_streams = []
        subscribed_streams = []
        tags = ''
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())

            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]
                # Get the keys of streams
                streams_key = currentUser.streams_owned

                streams = CreateStream.Stream.query().fetch(50)
                for stream in streams:
                    tags = tags + str(stream.name) + ' '
                    for tag in stream.tags:
                        tags = tags + str(tag) + ' '

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
        # stream_cover = images.get_serving_url(stream.cover)
        # for stream in currentUser.streams_owned:
        template_values = {
            'streams': owned_streams,
            'subscribed_streams': subscribed_streams,
            'user_id': currentUser.identity,
            'url': url,
            'url_linktext': url_linktext,
            'tags':str(tags)
        }

        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/management.html')
        self.response.write(template.render(template_values))
        # self.redirect('/index')



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
                streams_owned_key = currentUser.streams_owned
                streams_key = []
                streams = CreateStream.Stream.query().fetch(50)
                for stream in streams:
                    streams_key.append(stream.key)
                if self.request.get("delete"):
                    for stream_key in streams_owned_key[:]:
                        stream = stream_key.get()
                        if self.request.get(str(stream.key.id())):
                                currentUser.streams_owned.remove(stream_key)
                                for stream_key_searched in currentUser.streams_searched:
                                    if stream_key_searched == stream_key:
                                        currentUser.streams_searched.remove(stream_key)
                                stream_key.delete()
                if self.request.get("unsubscribe"):
                    for stream in streams[:]:
                        if self.request.get(str(stream.key.id())):
                                currentUser.streams_subscribed.remove(stream.key)
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
                               # ('/file-upload', ViewASingleStream.FileUploadHandler),
                               ('/view_photo/([^/]+)?', ViewASingleStream.ViewPhotoHandler),
                               ('/view', ViewASingleStream.ViewStream),
                               ('/search', SearchStreams.SearchStreams),
                               ('/trend', TrendingStreams.TrendStreams),
                               ('/viewall', ViewAllStreams.ViewAllStreams),
                               # ('/trend', TrendingStreams.TrendStreams),
                               # ('/index', Index),
                               ('/map', MapHandler.MapHandler),
                               ], debug=True)