import os
import jinja2
from Connexus import User
from CreateStream import Stream

__author__ = 'yusun'
import cgi
import urllib
import webapp2

from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class ViewStream(webapp2.RequestHandler):

    def get(self):
        stream_name = self.request.get('stream_name')
        self.response.write(stream_name)
        stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
        # user = User.query(User.email == users.get_current_user().email)
        # Check if the user logs in
        user = users.get_current_user()
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'



        template_values = {
            'stream_name': stream_name,
            'user_id': user.user_id(),
            'url': url,
            'url_linktext': url_linktext,
        }

        template = JINJA_ENVIRONMENT.get_template('/htmls/ViewASingleStream.html')
        self.response.write(template.render(template_values))

    def post(self):
        stream_name = self.request.get('stream_name')
        stream = Stream.query(Stream.name == stream_name).fetch(1)[0]
        picture = self.request.get('img')
        stream.picture.append(picture)
        stream.num_of_pics = stream.num_of_pics + 1
        stream.put()

        query_params = {'stream_name': stream_name}
        self.redirect('/view?' + urllib.urlencode(query_params))

# [START subsribe this stream]

class Subsribe(webapp2.RequestHandler):
    def post(self):

        user = User.query(User.email == users.get_current_user().email)
        if user:
            stream = self.request.get('stream')
            user.fetch(1)[0].stream_subsribed.append(stream.key())
            self.response.out.write('Subsribe success!')
        else:
             self.redirect(users.create_login_url(self.request.uri))
