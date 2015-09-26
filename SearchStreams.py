__author__ = 'yusun'
import cgi
import urllib
import webapp2
import CreateStream
import Connexus
from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users


class SearchStreams(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'
        template_values = {
            # 'stream_name': stream_name,
            'user_id': user.user_id(),
            'url': url,
            'url_linktext': url_linktext,
        }

        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/SearchStreams.html')
        self.response.write(template.render(template_values))

    def post(self):
        stream_name = self.request.get('stream_name')
        greeting = CreateStream.Greeting(parent=CreateStream.stream_key(stream_name))

        if users.get_current_user():
            greeting.author = users.get_current_user().nickname()

        greeting.content = self.request.get('content')

        # Get image data
        avatar = self.request.get('img')
        # Transform the image
        avatar = images.resize(avatar, 32, 32)
        greeting.avatar = avatar

        greeting.put()

        self.redirect('/?' + urllib.urlencode(
            {'stream_name': stream_name}))