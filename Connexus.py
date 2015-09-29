

__author__ = 'yusun'
from google.appengine.api import users
from google.appengine.ext import ndb
import os
import webapp2
import jinja2
import urllib
from google.appengine.api import images

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)
JINJA_ENVIRONMENT.filters['get_serving_url'] = images.get_serving_url
DEFAULT_STREAM_NAME = 'default_stream'


def guestbook_key(guestbook_name=DEFAULT_STREAM_NAME):
    """Constructs a Datastore key for a Guestbook entity.
    We use guestbook_name as the key.
    """
    return ndb.Key('Guestbook', guestbook_name)



# [START greeting]
class User(ndb.Model):
    """Sub model for representing an author."""
    identity = ndb.StringProperty(indexed=False)
    email = ndb.StringProperty(indexed=True)
    streams_owned = ndb.KeyProperty(repeated = True)
    streams_subscribed = ndb.KeyProperty(repeated = True)
    streams_searched = ndb.KeyProperty(repeated = True)
    keyword = ndb.StringProperty(indexed=False)

#
# JINJA_ENVIRONMENT = jinja2.Environment(
#     loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
#     extensions=['jinja2.ext.autoescape'],
#     autoescape=True)
#
#
#
# def doRender(handler, tname='index.html', values={}):
#
#
#         newval = dict(values)
#         newval['path'] = handler.request.path
#         template = JINJA_ENVIRONMENT.get_template(tname)
#         outstr = template.render(newval)
#         handler.response.out.write(outstr)
#         return True
#
# class LoginHandler(webapp2.RequestHandler):
#     def get(self):
#         template = JINJA_ENVIRONMENT.get_template('/htmls/login.html')
#         outstr = template.render({})
#         self.response.write(outstr)
#
#
#     def post(self):
#         self.redirect(users.create_login_url(self.request.uri))
#
#
# class MainPage(webapp2.RequestHandler):
#
#     def get(self):
#         # Checks for active Google account session
#         user = users.get_current_user()
#         # self.redirect('/login')
#         if user:
#             self.response.headers['Content-Type'] = 'text/html; charset=utf-8'
#             self.response.write('Hello, ' + user.nickname())
#         else:
#             self.redirect('/login')
#
#
#
#
#
#
# app = webapp2.WSGIApplication([
#     ('/login', LoginHandler),
#     ('/', MainPage),
# ], debug=True)