__author__ = 'yusun'
import cgi
import os
import urllib
import jinja2
import webapp2
import CreateStream
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
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())

            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]
                streams_key = currentUser.streams_searched
                self.response.write(currentUser)
                for stream_key in streams_key:
                    if stream_key in currentUser.streams_owned:
                        pass
                    else:
                        currentUser.streams_searched.remove(stream_key)
                result_number = len(streams_key)
                for stream_key in streams_key:
                    stream = stream_key.get()
                    if stream:
                        searched_streams.append(stream)
                    else:
                        currentUser = User(identity = user.user_id(), email = user.email())
                        currentUser.put()
                keyword = currentUser.keyword
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
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/SearchStreams.html')
        self.response.write(template.render(template_values))

    def post(self):
        user = users.get_current_user()
        owned_streams = []
        searched_streams = []
        documents = []
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            getUser = User.query(User.email == user.email())

            # self.response.write(str(getUser.fetch(1)))
            if getUser.fetch(1):

                currentUser = getUser.fetch(1)[0]
                currentUser.streams_searched = []
                keyword = self.request.get("keywords")
                currentUser.keyword = keyword
                currentUser.put()
                currentUser.put()
                # Get the keys of streams
                streams_key = currentUser.streams_owned
                for stream_key in streams_key:
                    stream = stream_key.get()
                    if stream:
                        owned_streams.append(stream)
                for stream in owned_streams:
                    document = search.Document(
                        doc_id = str(stream.key.id()),
                        fields = [search.TextField(name = "streams_owned",value=stream.name)]
                    )
                    documents.append(document)
                self.response.write(documents)
                if documents:
                    index = search.Index(name = 'streams_owned')
                    docs = index.put(documents)
                    try:
                        results = index.search(currentUser.keyword)
                        n = 0
                        for result in results:
                            if n < 5:
                                for stream in owned_streams:
                                    if long(result.doc_id) == stream.key.id():
                                        currentUser.streams_searched.append(stream.key)
                                        n = n+1
                            else:
                                break
                        currentUser.put()
                        currentUser.put()
                        self.response.write(results)
                        for doc in docs:
                            index.delete(doc.id)
                    except search.Error:
                        logging.exception("Search failed")
            else:
                currentUser = User(identity = user.user_id(), email = user.email())
                currentUser.put()
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        # for stream in currentUser.streams_owned:
        template_values = {
            'streams': searched_streams,
            'user_id': currentUser.identity,
            'url': url,
            'url_linktext': url_linktext,
        }
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