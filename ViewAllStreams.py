__author__ = 'yusun'
import cgi
import urllib
import webapp2
import CreateStream
import Connexus
from google.appengine.ext import ndb
from google.appengine.api import images
from google.appengine.api import users


class ViewAllStreams(webapp2.RequestHandler):
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

        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/ViewAllStreams.html')
        self.response.write(template.render(template_values))