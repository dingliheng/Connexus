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
            'user_id': user.user_id(),
            'url': url,
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
                # tokens = ','.join([keyword[:i] for i in xrange(1, len(keyword)+1)])
                # tokens = tokens.split(',')
                # for token in tokens[::-1]:
                #     pattern = re.compile('\S*'+token+'\S*')
                #     for stream in streams[:]:
                #         content = stream.name
                #         for tag in stream.tags:
                #             content = content+' '+tag
                #         match = pattern.search(content)
                #         if match:
                #             currentUser.streams_searched.append(stream.key)
                #             streams.remove(stream)
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

                # <----------fuck-------------------------------------------------------------------------------->
            #     for stream in streams:
            #         content = ""
            #         for tag in stream.tags:
            #             content = content+" "+tag
            #         document = search.Document(
            #             doc_id = str(stream.key.id()),
            #             fields = [search.TextField(name = "streams_owned",value=stream.name+' '+content)]
            #         )
            #         documents.append(document)
            #     self.response.write(documents)
            #     if documents:
            #         index = search.Index(name = 'streams_owned')
            #         docs = index.put(documents)
            #         try:
            #             results = index.search(currentUser.keyword)
            #             n = 0
            #             for result in results:
            #                 if n < 5:
            #                     for stream in streams:
            #                         if long(result.doc_id) == stream.key.id():
            #                             currentUser.streams_searched.append(stream.key)
            #                             n = n+1
            #                 else:
            #                     break
            #             currentUser.put()
            #             currentUser.put()
            #             # self.response.write(results)
            #             for doc in docs:
            #                 index.delete(doc.id)
            #         except search.Error:
            #             logging.exception("Search failed")
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












        # self.response.write("dsfsfsdf")
        #
        # stream_name = self.request.get('stream_name')
        # greeting = CreateStream.Greeting(parent=CreateStream.stream_key(stream_name))
        #
        # if users.get_current_user():
        #     greeting.author = users.get_current_user().nickname()
        #
        # greeting.content = self.request.get('content')
        #
        # # Get image data
        # avatar = self.request.get('img')
        # # Transform the image
        # avatar = images.resize(avatar, 32, 32)
        # greeting.avatar = avatar
        #
        # greeting.put()
        #
        # self.redirect('/?' + urllib.urlencode(
        #     {'stream_name': stream_name}))