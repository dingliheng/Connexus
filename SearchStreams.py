import logging

__author__ = 'yusun'
import cgi
import os
import re
import urllib
import jinja2
import webapp2
import CreateStream
import json
from Connexus import User
from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users

from google.appengine.api import search

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class SearchStreams(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        searched_streams = []
        keyword = ""
        tags = ''
        result_number = 0
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())

            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]
                streams_key = currentUser.streams_searched
                streams = CreateStream.Stream.query().fetch(50)
                for stream in streams:
                    tags = tags + str(stream.name) + ' '
                    for tag in stream.tags:
                        tags = tags + str(tag) + ' '

                for stream_key in streams_key:
                    stream = stream_key.get()
                    if stream:
                        searched_streams.append(stream)
                    else:
                        currentUser = User(identity = user.user_id(), email = user.email())
                        currentUser.put()
                keyword = currentUser.keyword
                result_number = len(searched_streams)
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        template_values = {
            'streams': searched_streams,
            "result_number":result_number,
            'keyword': keyword,
            'url': url,
            'user_id': user.email(),
            'url_linktext': url_linktext,
            'tags':str(tags)
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/SearchStreams.html')
        self.response.write(template.render(template_values))


    def post(self):
        user = users.get_current_user()
        # documents = []
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())
            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]
                currentUser.streams_searched = []
                keyword = self.request.get("keywords")
                currentUser.keyword = keyword
                currentUser.put()
                currentUser.put()
                streams = CreateStream.Stream.query().fetch(50)
                pattern = re.compile('\S*'+keyword+'\S*',re.I)
                for stream in streams:
                    content = stream.name
                    for tag in stream.tags:
                        content = content+' '+tag
                    match = pattern.search(content)
                    if match:
                        currentUser.streams_searched.append(stream.key)
                if len(currentUser.streams_searched)>20:
                    currentUser.streams_searched = currentUser.streams_searched[0:19]
                currentUser.streams_searched.sort(key=lambda d:d.get().name.upper(),reverse=False)
                currentUser.put()
                currentUser.put()


            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        # for stream in currentUser.streams_owned:
        # template_values = {
        #     'streams': searched_streams,
        #     'user_id': currentUser.identity,
        #     'url': url,
        #     'url_linktext': url_linktext,
        # }
        self.redirect('/search')

app = webapp2.WSGIApplication([
                               ('/search', SearchStreams),
                               ], debug=True)

