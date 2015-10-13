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
        tags = ''
        if user:
            url = users.create_logout_url(self.request.uri)
            url_linktext = 'Logout'
            streams = CreateStream.Stream.query().fetch(50)
            for stream in streams:
                tags = tags + str(stream.name) + ' '
                for tag in stream.tags:
                    tags = tags + str(tag) + ' '
        
        else:
            url = users.create_login_url(self.request.uri)
            url_linktext = 'Login'

        # Fetch 5 streams
        streams = CreateStream.Stream.query().fetch(10)
        # for stream in streams:
        #     stream.key.delete()

        template_values = {
            # 'stream_name': stream_name,
            'streams': streams,
            'user_id': user.email(),
            'url': url,
            'url_linktext': url_linktext,
            'tags':str(tags)
        }

        template = Connexus.JINJA_ENVIRONMENT.get_template('/htmls/ViewAllStreams.html')
        self.response.write(template.render(template_values))